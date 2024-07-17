import 'package:dacs/models/postModel.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Searchpost extends StatefulWidget {
  final String userName;

  const Searchpost({Key? key, required this.userName}) : super(key: key);

  @override
  State<Searchpost> createState() => _SearchpostState();
}

class _SearchpostState extends State<Searchpost> {
  TextEditingController _searchController = TextEditingController();
  Future<List<Postmodel>>? futurePostModel;

  @override
  void initState() {
    super.initState();
    _searchController.addListener(_onSearchChanged);
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  void _onSearchChanged() {
    setState(() {
      if (_searchController.text.trim().isNotEmpty) {
        futurePostModel = searchPosts(_searchController.text.trim());
      } else {
        futurePostModel = null; // Clear the posts if the search bar is empty
      }
    });
  }

  Future<List<Postmodel>> searchPosts(String query) async {
    // Construct the API endpoint based on the query type
    String url = '';
    if (query.length >= 1) {
      url = 'http://192.168.100.107:8080/api/show/posts/user/$query';
    } else if (query.length >= 1) {
      url = 'http://192.168.100.107:8080/api/show/posts/title/$query';
    } else {
      url = 'http://192.168.100.107:8080/api/show/posts/content/$query';
    }

    final response = await http.get(Uri.parse(url));

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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            title: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                hintText: 'Tìm kiếm...',
                border: InputBorder.none,
                hintStyle: TextStyle(color: Colors.grey),
              ),
              style: TextStyle(color: Colors.black),
            ),
            actions: [
              IconButton(
                onPressed: () {
                  _onSearchChanged();
                },
                icon: Icon(
                  Icons.search,
                  size: 30,
                ),
                color: Colors.black,
              ),
            ],
            floating: true,
            snap: true,
          ),
          futurePostModel == null
              ? SliverToBoxAdapter(
            child: Center(child: Text('Nhập thông tin muốn tìm kiếm')),
          )
              : FutureBuilder<List<Postmodel>>(
            future: futurePostModel,
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return SliverToBoxAdapter(
                  child: Center(child: CircularProgressIndicator()),
                );
              } else if (snapshot.hasError) {
                return SliverToBoxAdapter(
                  child: Center(child: Text('Failed to load posts: ${snapshot.error}')),
                );
              } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                return SliverToBoxAdapter(
                  child: Center(child: Text('No posts available')),
                );
              } else {
                final posts = snapshot.data!;
                return SliverList(
                  delegate: SliverChildBuilderDelegate(
                        (context, index) {
                      final post = posts[index];
                      return ListTile(
                        title: Text(post.title),
                        subtitle: Text(post.content),
                        leading: CircleAvatar(
                          backgroundImage: NetworkImage(
                            'http://192.168.100.107:8080/api/show/avatar${post.avatar}',
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
