import 'package:dacs/Screens/Home/profile.dart';
import 'package:dacs/widgets/RootApp_widgets/createPostPrivate.dart';
import 'package:dacs/widgets/RootApp_widgets/editPost.dart';
import 'package:flutter/material.dart';
import 'package:dacs/models/postModel.dart';
import 'package:dacs/models/profileModel.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:intl/intl.dart';

class Wallperson extends StatefulWidget {
  final String userName;

  const Wallperson({Key? key, required this.userName}) : super(key: key);

  @override
  State<Wallperson> createState() => _WallpersonState();
}

class _WallpersonState extends State<Wallperson> {
  late Future<List<Postmodel>> futurePosts;
  late Future<ProfileModel> futureProfile;

  @override
  void initState() {
    super.initState();
    futurePosts = getPostsProfile();
    futureProfile = _fetchProfile();
  }

  Future<List<Postmodel>> getPostsProfile() async {
    final response = await http.get(
        Uri.parse('http://192.168.100.107:8080/api/show/posts/user/${widget.userName}'));

    if (response.statusCode == 200) {
      try {
        List<dynamic> jsonData = json.decode(response.body);
        List<Postmodel> posts = jsonData.map((e) => Postmodel.fromJson(e)).toList();
        return posts;
      } catch (e) {
        print('Error parsing JSON for posts: $e');
        throw Exception('Failed to parse posts');
      }
    } else {
      print('Server responded with status code: ${response.statusCode}');
      throw Exception('Failed to load posts');
    }
  }

  Future<ProfileModel> _fetchProfile() async {
    final response = await http.get(
        Uri.parse('http://192.168.100.107:8080/api/show/profile/${widget.userName}'));

    if (response.statusCode == 200) {
      try {
        Map<String, dynamic> jsonData = json.decode(response.body);
        ProfileModel profile = ProfileModel.fromJson(jsonData);
        return profile;
      } catch (e) {
        print('Error parsing JSON for profile: $e');
        throw Exception('Failed to parse profile');
      }
    } else {
      print('Server responded with status code: ${response.statusCode}');
      throw Exception('Failed to load profile');
    }
  }

  Future<void> deletePost(String postId, BuildContext context) async {
    final url = 'http://192.168.100.107:8080/api/delete/post/$postId';

    try {
      final response = await http.delete(Uri.parse(url));

      if (response.statusCode == 200) {
        print('Post deleted successfully');
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Post deleted successfully')),
        );
        setState(() {
          futurePosts = getPostsProfile();
        });
      } else {
        print('Failed to delete post. Status code: ${response.statusCode}');
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to delete post. Status code: ${response.statusCode}')),
        );
      }
    } catch (e) {
      print('Error deleting post: $e');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error deleting post: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          // Background image or color
          Container(
            color: Colors.white, // Replace with your preferred background color or image
          ),
          // Personal posts
          Positioned.fill(
            child: CustomScrollView(
              slivers: [
                SliverAppBar(
                  expandedHeight: 200,
                  flexibleSpace: FlexibleSpaceBar(
                    background: Image.asset(
                      'assets/imagePerson.jpg', // Replace with your background image asset path
                      fit: BoxFit.cover,
                    ),
                  ),
                  title: Text('Trang cá nhân'),
                  centerTitle: false,
                  actions: [
                    IconButton(
                      icon: Icon(Icons.add),
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => CreatePostPrivate(userName: widget.userName),
                          ),
                        );
                      },
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
                          child: Text('Failed to load profile: ${snapshot.error}'),
                        );
                      } else if (!snapshot.hasData || snapshot.data == null) {
                        return Center(child: Text('No profile data available'));
                      } else {
                        final profile = snapshot.data!;
                        return Container(
                          padding: EdgeInsets.all(12),
                          color: Colors.white,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Row(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  CircleAvatar(
                                    radius: 40,
                                    backgroundColor: Colors.grey[200],
                                    backgroundImage: NetworkImage(
                                        'http://192.168.100.107:8080/api/show/avatar${profile.avatar}'),
                                  ),
                                  SizedBox(width: 16),
                                  Expanded(
                                    child: Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        Text(
                                          profile.userNameProfile,
                                          style: TextStyle(
                                            fontSize: 24,
                                            fontWeight: FontWeight.bold,
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                ],
                              ),
                              SizedBox(height: 16),
                              Row(
                                children: [
                                  Expanded(
                                    child: TextButton(
                                      onPressed: () {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder: (context) => CreatePostPrivate(userName: widget.userName),
                                          ),
                                        );
                                      },
                                      style: TextButton.styleFrom(
                                        backgroundColor: Colors.grey[200],
                                        padding: EdgeInsets.symmetric(vertical: 12),
                                        shape: RoundedRectangleBorder(
                                          borderRadius: BorderRadius.circular(20),
                                        ),
                                      ),
                                      child: Row(
                                        mainAxisAlignment: MainAxisAlignment.center,
                                        children: [
                                          Icon(Icons.edit, color: Colors.blue),
                                          SizedBox(width: 8),
                                          Text(
                                            'Bạn đang nghĩ gì?',
                                            style: TextStyle(
                                              color: Colors.grey,
                                              fontSize: 16,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ),
                                  SizedBox(width: 8),
                                  IconButton(
                                    onPressed: () {
                                      Navigator.push(context, MaterialPageRoute(builder: (context) => CreatePostPrivate(userName: widget.userName),),);
                                    },
                                    icon: Icon(Icons.photo_library, color: Colors.green),
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
                                                  Text(parseDateString(post.time)),
                                                ],
                                              ),
                                            ),
                                          ],
                                        ),
                                        Spacer(), // Thêm Spacer để đẩy nút vào cạnh phải
                                        PopupMenuButton<String>(
                                          onSelected: (String result) {
                                            switch (result) {
                                              case 'edit':
                                              // Thực hiện hành động sửa
                                                Navigator.push(
                                                  context,
                                                  MaterialPageRoute(
                                                    builder: (context) => Editpost(userName: widget.userName, postId: post.id.toString()),
                                                  ),
                                                );
                                                break;
                                              case 'delete':
                                              // Thực hiện hành động xóa
                                                deletePost(post.id.toString(), context);
                                                break;
                                            }
                                          },
                                          itemBuilder: (BuildContext context) => <PopupMenuEntry<String>>[
                                            PopupMenuItem<String>(
                                              value: 'edit',
                                              child: Row(
                                                children: [
                                                  Icon(Icons.edit, color: Colors.blue),
                                                  SizedBox(width: 10),
                                                  Text('Sửa'),
                                                ],
                                              ),
                                            ),
                                            PopupMenuItem<String>(
                                              value: 'delete',
                                              child: Row(
                                                children: [
                                                  Icon(Icons.delete, color: Colors.red),
                                                  SizedBox(width: 10),
                                                  Text('Xóa'),
                                                ],
                                              ),
                                            ),
                                          ],
                                        ),
                                      ],
                                    ),
                                    SizedBox(height: 8),
                                    Text('${post.content}'),
                                    SizedBox(height: 8),
                                    if (post.image != null)
                                      Image.network(
                                        'http://192.168.100.107:8080/api/show/postImage${post.image}',
                                        width: double.infinity,
                                        fit: BoxFit.cover,
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
          ),
        ],
      ),
    );
  }

  String parseDateString(String dateString) {
    DateTime dateTime = DateTime.parse(dateString);
    String formattedDate = DateFormat('dd/MM/yyyy HH:mm').format(dateTime);
    return formattedDate;
  }
}