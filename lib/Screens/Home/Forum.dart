import 'dart:convert';
import 'package:dacs/models/postModel.dart';
import 'package:dacs/models/profileModel.dart';
import 'package:dacs/widgets/RootApp_widgets/comment.dart';
import 'package:dacs/widgets/RootApp_widgets/createPost.dart';
import 'package:dacs/widgets/RootApp_widgets/searchPost.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class Forum extends StatefulWidget {
  final String userName;

  Forum({required this.userName});

  @override
  State<Forum> createState() => _ForumState();
}

class _ForumState extends State<Forum> with SingleTickerProviderStateMixin {
  late Future<List<Postmodel>> futurePosts;
  late Future<ProfileModel> futureProfile;

  @override
  void initState() {
    super.initState();
    futurePosts = getAllPosts();
    futureProfile = _fetchProfile();
  }

  Future<List<Postmodel>> getAllPosts() async {
    final response = await http.get(
        Uri.parse('http://192.168.100.107:8080/api/show/posts'));

    if (response.statusCode == 200) {
      try {
        List<dynamic> jsonData = json.decode(response.body);
        List<Postmodel> posts = jsonData.map((e) => Postmodel.fromJson(e)).toList();
        return posts;
      } catch (e) {
        print('Error parsing JSON: $e');
        throw Exception('Failed to parse posts');
      }
    } else {
      print('Server responded with status code: ${response.statusCode}');
      throw Exception('Failed to load posts');
    }
  }

  Future<ProfileModel> _fetchProfile() async {
    final response = await http.get(Uri.parse(
        'http://192.168.100.107:8080/api/show/profile/${widget.userName}'));

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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            backgroundColor: Colors.white,
            title: Text(
              'Forum',
              style: TextStyle(
                color: Colors.blue,
                fontSize: 28,
                fontWeight: FontWeight.bold,
                letterSpacing: -1.2,
              ),
            ),
            automaticallyImplyLeading: false,
            centerTitle: false,
            floating: true,
            actions: [
              Container(
                margin: const EdgeInsets.all(6),
                decoration: BoxDecoration(
                  color: Colors.grey[200],
                  shape: BoxShape.circle,
                ),
                child: IconButton(
                  onPressed: () {
                    Navigator.push(context, MaterialPageRoute(builder: (context) => Searchpost(userName: widget.userName),),);
                  },
                  icon: Icon(Icons.search),
                  iconSize: 30,
                  color: Colors.black,
                ),
              ),
            ],
          ),
          SliverToBoxAdapter(
            child: FutureBuilder<ProfileModel>(
              future: futureProfile,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return Center(child: CircularProgressIndicator());
                } else if (snapshot.hasError) {
                  return Center(
                      child: Text('Failed to load profile: ${snapshot.error}'));
                } else if (!snapshot.hasData || snapshot.data == null) {
                  return Center(child: Text('No profile data available'));
                } else {
                  final profile = snapshot.data!;
                  return Container(
                    padding: EdgeInsets.fromLTRB(12, 8, 12, 8),
                    color: Colors.white,
                    child: Column(
                      children: [
                        Row(
                          children: [
                            CircleAvatar(
                              radius: 20,
                              backgroundColor: Colors.grey[200],
                              backgroundImage: profile.avatar.isNotEmpty &&
                                  profile.avatar != 'default.png'
                                  ? NetworkImage(
                                  'http://192.168.100.107:8080/api/show/avatar${profile.avatar}')
                                  : AssetImage('assets/imagePerson.jpg') as ImageProvider,
                            ),
                            const SizedBox(
                              width: 8,
                            ),
                            Expanded(
                              child: TextButton(
                                onPressed: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) => CreatePost(userName: widget.userName)),
                                  );
                                },
                                style: TextButton.styleFrom(
                                  alignment: Alignment.centerLeft,
                                ),
                                child: Text(
                                  'Bạn đang nghĩ gì?',
                                  style: TextStyle(
                                    color: Colors.grey,
                                    fontSize: 16,
                                  ),
                                ),
                              ),
                            ),
                            TextButton(
                              onPressed: () {},
                              child: const Icon(
                                Icons.photo_library,
                                color: Colors.green,
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  );
                }
              },
            ),
          ),
          FutureBuilder<List<Postmodel>>(
            future: futurePosts,
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return SliverToBoxAdapter(
                    child: Center(child: CircularProgressIndicator()));
              } else if (snapshot.hasError) {
                return SliverToBoxAdapter(
                    child: Center(child: Text('Failed to load posts: ${snapshot.error}')));
              } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                return SliverToBoxAdapter(
                    child: Center(child: Text('No posts available')));
              } else {
                final posts = snapshot.data!.reversed.toList(); // Đảo ngược danh sách các bài đăng
                return SliverList(
                  delegate: SliverChildBuilderDelegate(
                        (context, index) {
                      final post = posts[index];
                      return Container(
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
                                    backgroundImage: NetworkImage(
                                        'http://192.168.100.107:8080/api/show/avatar${post.avatar}'),
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
                              Text(
                                post.title,
                                style: TextStyle(
                                    fontWeight: FontWeight.bold, fontSize: 18),
                              ),
                              Text(post.content),
                              SizedBox(height: 10),
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
                                        Icons.thumb_up,
                                        color: Colors.blue,
                                      ),
                                      onPressed: () {},
                                    ),
                                    TextButton(
                                      onPressed: () {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder: (context) => CommentPost(
                                              postId: post.id.toString(),
                                              userName: widget.userName,
                                            ),
                                          ),
                                        );
                                      },
                                      child: Row(
                                        children: [
                                          Icon(
                                            Icons.comment,
                                            color: Colors.grey,
                                          ),
                                          SizedBox(width: 5),
                                          Text(
                                            'Comment',
                                            style: TextStyle(color: Colors.grey),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                    childCount: posts.length,
                  ),
                );
              }
            },
          ),
        ],
      ),
    );
  }
}
