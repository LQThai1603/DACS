import 'package:flutter/material.dart';
import 'package:dacs/models/postModel.dart';
import 'package:dacs/models/profileModel.dart';
import 'package:dacs/widgets/RootApp_widgets/createPost.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

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
                  title: Text('Wall'),
                  centerTitle: false,
                  actions: [
                    IconButton(
                      icon: Icon(Icons.add),
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => CreatePost(userName: widget.userName),
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
                                            builder: (context) => CreatePost(userName: widget.userName),
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
                                      Navigator.push(context, MaterialPageRoute(builder: (context) => CreatePost(userName: widget.userName),),);
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
                SliverList(
                  delegate: SliverChildBuilderDelegate(
                        (context, index) {
                      // Replace with actual post data
                      return ListTile(
                        title: Text('Post Title'),
                        subtitle: Text('Post Content'),
                        leading: CircleAvatar(
                          backgroundImage: AssetImage(
                            'assets/imagePerson.jpg', // Replace with actual URL
                          ),
                        ),
                      );
                    },
                    childCount: 10, // Replace with actual number of posts
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
