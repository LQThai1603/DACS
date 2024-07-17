import 'package:dacs/models/profileModel.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:dacs/models/commentModel.dart';
import 'package:dacs/models/postModel.dart';
import 'package:uuid/uuid.dart';


class CommentPost extends StatefulWidget {
  final String postId;
  final String userName;

  const CommentPost({Key? key, required this.postId, required this.userName}) : super(key: key);

  @override
  State<CommentPost> createState() => _CommentPostState();
}

class _CommentPostState extends State<CommentPost> {
  late Future<Postmodel> futurePost;
  late Future<List<Commentmodel>> futureComments;
  late Future<ProfileModel> futureProfile;
  final TextEditingController _commentController = TextEditingController();

  @override
  void initState() {
    super.initState();
    futurePost = getPostById();
    futureComments = getComments();
    futureProfile = getProfile();
  }

  Future<Postmodel> getPostById() async {
    final response =
    await http.get(Uri.parse('http://192.168.100.107:8080/api/show/post/${widget.postId}'));

    if (response.statusCode == 200) {
      try {
        Map<String, dynamic> jsonData = json.decode(response.body);
        Postmodel post = Postmodel.fromJson(jsonData);
        return post;
      } catch (e) {
        print('Error parsing JSON: $e');
        throw Exception('Failed to parse post');
      }
    } else {
      print('Server responded with status code: ${response.statusCode}');
      throw Exception('Failed to load post');
    }
  }

  Future<List<Commentmodel>> getComments() async {
    final response =
    await http.get(Uri.parse('http://192.168.100.107:8080/api/show/comments/${widget.postId}'));

    if (response.statusCode == 200) {
      try {
        List<dynamic> jsonData = json.decode(response.body);
        List<Commentmodel> comments =
        jsonData.map((e) => Commentmodel.fromJson(e)).toList();
        return comments;
      } catch (e) {
        print('Error parsing JSON: $e');
        throw Exception('Failed to parse comments');
      }
    } else {
      print('Server responded with status code: ${response.statusCode}');
      throw Exception('Failed to load comments');
    }
  }
  String generateUUID() {
    var uuid = Uuid();
    return uuid.v4();
  }
  String getCurrentTime() {
    DateTime now = DateTime.now();
    return now.toIso8601String(); // ISO 8601 format
  }
  Future<ProfileModel> getProfile() async {
    final response = await http.get(Uri.parse('http://192.168.100.107:8080/api/show/profile/${widget.userName}'));

    if (response.statusCode == 200) {
      try {
        Map<String, dynamic> jsonData = json.decode(response.body);
        ProfileModel profile = ProfileModel.fromJson(jsonData);
        return profile;
      } catch (e) {
        print('Error parsing JSON: $e');
        throw Exception('Failed to parse profile');
      }
    } else {
      throw Exception('Failed to load profile');
    }
  }

  Future<void> createComment(String content, ProfileModel profile) async {
    ProfileModel profiles = await futureProfile;
    final Map<String, dynamic> data = {
      'avatar': profiles.avatar,
      'content': content,
      'idPost': widget.postId,
      'userNameProfile': widget.userName,
    };

    final response = await http.post(
      Uri.parse('http://192.168.100.107:8080/api/create/comment/${widget.postId}'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(data),
    );

    if (response.statusCode == 201) {
      print('Comment created successfully');
      setState(() {
        futureComments = getComments();
      });
    } else {
      print('Failed to create comment. Server responded with status code: ${response.statusCode}');
      print('Response body: ${response.body}');
    }
  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Bình luận', style: TextStyle(color: Colors.white)),
        backgroundColor: Colors.blue,
        iconTheme: IconThemeData(color: Colors.white),
      ),
      body: Column(
        children: [
          Expanded(
            child: FutureBuilder<Postmodel>(
              future: futurePost,
              builder: (context, postSnapshot) {
                if (postSnapshot.connectionState == ConnectionState.waiting) {
                  return Center(child: CircularProgressIndicator());
                } else if (postSnapshot.hasError) {
                  return Center(child: Text('Error: ${postSnapshot.error}'));
                } else if (!postSnapshot.hasData) {
                  return Center(child: Text('No data available'));
                } else {
                  final post = postSnapshot.data!;
                  return FutureBuilder<List<Commentmodel>>(
                    future: futureComments,
                    builder: (context, commentSnapshot) {
                      if (commentSnapshot.connectionState == ConnectionState.waiting) {
                        return Center(child: CircularProgressIndicator());
                      } else if (commentSnapshot.hasError) {
                        return Center(child: Text('Error: ${commentSnapshot.error}'));
                      } else {
                        final comments = commentSnapshot.data!;

                        return CustomScrollView(
                          slivers: [
                            SliverList(
                              delegate: SliverChildBuilderDelegate(
                                    (context, index) {
                                  return Column(
                                    children: [
                                      Container(
                                        decoration: BoxDecoration(
                                          border: Border.all(color: Colors.grey, width: 1.0),
                                          borderRadius: BorderRadius.circular(8.0),
                                          color: Colors.grey[200],
                                        ),
                                        margin: EdgeInsets.symmetric(vertical: 8.0, horizontal: 12.0),
                                        child: Container(
                                          padding: EdgeInsets.all(10),
                                          color: Colors.white,
                                          child: Column(
                                            crossAxisAlignment: CrossAxisAlignment.start,
                                            children: [
                                              Row(
                                                children: [
                                                  CircleAvatar(
                                                    backgroundImage: NetworkImage('http://192.168.100.107:8080/api/show/avatar${post.avatar}'),
                                                  ),
                                                  Container(
                                                    padding: EdgeInsets.only(left: 5),
                                                    child: Column(
                                                      crossAxisAlignment: CrossAxisAlignment.start,
                                                      children: [
                                                        Text('${post.userNameProfile}'),
                                                        Text(post.time),
                                                      ],
                                                    ),
                                                  ),
                                                ],
                                              ),
                                              SizedBox(height: 10),
                                              Text(post.title),
                                              Text(post.content),
                                              Image.network(
                                                "http://192.168.100.107:8080/api/show/postImage${post.image}",
                                                fit: BoxFit.cover,
                                              ),
                                              SizedBox(height: 10),
                                              Container(
                                                color: Colors.white,
                                                child: Row(
                                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                                  children: [
                                                    IconButton(
                                                      icon: Icon(
                                                        Icons.thumb_up_alt,
                                                        color: Colors.blue,
                                                      ),
                                                      onPressed: () {
                                                        // Handle like button press if needed
                                                      },
                                                    ),
                                                  ],
                                                ),
                                              ),
                                            ],
                                          ),
                                        ),
                                      ),
                                      for (var comment in comments)
                                        Container(
                                          decoration: BoxDecoration(
                                            border: Border.all(color: Colors.grey, width: 1.0),
                                            borderRadius: BorderRadius.circular(8.0),
                                            color: Colors.grey[200],
                                          ),
                                          margin: EdgeInsets.symmetric(vertical: 8.0, horizontal: 12.0),
                                          child: Container(
                                            padding: EdgeInsets.all(10),
                                            color: Colors.white,
                                            child: Column(
                                              crossAxisAlignment: CrossAxisAlignment.start,
                                              children: [
                                                Row(
                                                  children: [
                                                    CircleAvatar(
                                                      backgroundImage: NetworkImage('http://192.168.100.107:8080/api/show/avatar${comment.avatar}'),
                                                    ),
                                                    Container(
                                                      padding: EdgeInsets.only(left: 5),
                                                      child: Column(
                                                        crossAxisAlignment: CrossAxisAlignment.start,
                                                        children: [
                                                          Text('${comment.userNameProfile}'),
                                                          Text(comment.time),
                                                        ],
                                                      ),
                                                    ),
                                                  ],
                                                ),
                                                SizedBox(height: 10),
                                                Text(comment.content),
                                              ],
                                            ),
                                          ),
                                        ),
                                    ],
                                  );
                                },
                                childCount: 1,
                              ),
                            ),
                          ],
                        );
                      }
                    },
                  );
                }
              },
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: _commentController,
                    decoration: InputDecoration(
                      hintText: 'Viết bình luận...',
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(30.0),
                      ),
                    ),
                  ),
                ),
                IconButton(
                  icon: Icon(Icons.send, color: Colors.blue),
                  onPressed: () async {
                    String commentContent = _commentController.text.trim();
                    if (commentContent.isNotEmpty) {
                      try {
                        ProfileModel profile = await futureProfile;
                        createComment(commentContent, profile);
                        _commentController.clear();
                      } catch (e) {
                        print('Error getting profile: $e');
                        // Handle error getting profile
                      }
                    } else {
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return AlertDialog(
                            title: Text('Thông báo'),
                            content: Text('Vui lòng nhập nội dung bình luận.'),
                            actions: <Widget>[
                              TextButton(
                                onPressed: () {
                                  Navigator.of(context).pop();
                                },
                                child: Text('OK'),
                              ),
                            ],
                          );
                        },
                      );
                    }
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
