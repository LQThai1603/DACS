import 'package:dacs/models/commentModel.dart';
import 'package:dacs/models/postModel.dart';
import 'package:dacs/widgets/RootApp_widgets/comment.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Posts extends StatefulWidget {
  final String userName;
  const Posts({super.key, required this.userName});

  @override
  State<Posts> createState() => _PostsState();
}

class _PostsState extends State<Posts> {
  late Future<List<Commentmodel>> commentPostList;
  late Future<List<Postmodel>> postModelList;

  @override
  void initState() {
    super.initState();
    postModelList = futurePostList();
    commentPostList = getComments();
  }

  @override
  Widget build(BuildContext context) {
    return CustomScrollView(
      slivers: [
        SliverAppBar(
          title: Text('Thông báo', style: TextStyle(color: Colors.white)),
          centerTitle: true,
          backgroundColor: Colors.blue,
          automaticallyImplyLeading: false,
        ),
        FutureBuilder<List<Commentmodel>>(
          future: commentPostList,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return SliverToBoxAdapter(
                child: Center(child: CircularProgressIndicator()),
              );
            } else if (snapshot.hasError) {
              return SliverToBoxAdapter(
                child: Center(child: Text('Error: ${snapshot.error}')),
              );
            } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
              return SliverToBoxAdapter(
                child: Center(child: Text('No comments found')),
              );
            } else {
              return SliverList(
                delegate: SliverChildBuilderDelegate(
                      (context, index) {
                    final comment = snapshot.data![index];
                    return GestureDetector(
                      onTap: () {
                        // Thực hiện chuyển hướng khi click vào ListTile
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => CommentPost(userName: widget.userName, postId: comment.idPost.toString())),
                        );
                      },
                      child: ListTile(
                        leading: CircleAvatar(
                          backgroundImage: NetworkImage('http://192.168.100.107:8080/api/show/avatar${comment.avatar}'),
                        ),
                        subtitle: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: [
                            Expanded(
                              child: Text(
                                '${comment.userNameProfile} đã bình luận vào bài viết của bạn lúc ${parseDateString(comment.time)}',
                                maxLines: 2,
                                overflow: TextOverflow.ellipsis,
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                  childCount: snapshot.data!.length,
                ),
              );
            }
          },
        ),
      ],
    );
  }

  String parseDateString(String? dateString) {
    if (dateString == null) return 'Unknown time';
    try {
      DateTime parsedDate = DateTime.parse(dateString);
      return DateFormat('yyyy-MM-dd HH:mm').format(parsedDate);
    } catch (e) {
      print('Error parsing date: $e');
      return 'Unknown time';
    }
  }

  Future<List<Postmodel>> futurePostList() async {
    final response = await http.get(Uri.parse('http://192.168.100.107:8080/api/show/posts/user/${widget.userName}'));

    if (response.statusCode == 200) {
      try {
        List<dynamic> jsonData = json.decode(response.body);
        List<Postmodel> postmodel = jsonData.map((e) => Postmodel.fromJson(e)).toList();
        return postmodel;
      } catch (e) {
        print('Error parsing JSON: $e');
        throw Exception('Failed to parse posts');
      }
    } else {
      print('Server responded with status code: ${response.statusCode}');
      throw Exception('Failed to load posts');
    }
  }

  Future<List<Commentmodel>> getComments() async {
    List<Postmodel> postmodel = await futurePostList();
    List<Commentmodel> allComments = [];

    for (var post in postmodel) {
      final response = await http.get(Uri.parse('http://192.168.100.107:8080/api/show/comments/${post.id}'));

      if (response.statusCode == 200) {
        try {
          List<dynamic> jsonData = json.decode(response.body);
          List<Commentmodel> comments = jsonData.map((e) => Commentmodel.fromJson(e)).toList();

          // Lọc ra các bình luận không trùng userName
          comments.removeWhere((comment) => comment.userNameProfile == widget.userName);

          allComments.addAll(comments);
        } catch (e) {
          print('Error parsing JSON: $e');
          throw Exception('Failed to parse comments');
        }
      } else {
        print('Server responded with status code: ${response.statusCode}');
        throw Exception('Failed to load comments for post: ${post.id}');
      }
    }

    return allComments;
  }
}

class CommentDetailScreen extends StatelessWidget {
  final Commentmodel comment;

  const CommentDetailScreen({required this.comment});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Chi tiết bình luận'),
        backgroundColor: Colors.blue,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CircleAvatar(
              backgroundImage: NetworkImage('http://192.168.100.107:8080/api/show/avatar${comment.avatar}'),
            ),
            SizedBox(height: 20),
            Text('${comment.userNameProfile} đã bình luận vào bài viết của bạn lúc ${DateFormat('yyyy-MM-dd HH:mm').format(DateTime.parse(comment.time))}'),
            SizedBox(height: 20),
            Text(comment.content),
          ],
        ),
      ),
    );
  }
}
