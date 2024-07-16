import 'package:flutter/material.dart';

class Searchpost extends StatefulWidget {
  final String userName;

  const Searchpost({Key? key, required this.userName}) : super(key: key);

  @override
  State<Searchpost> createState() => _SearchpostState();
}

class _SearchpostState extends State<Searchpost> {
  TextEditingController _searchController = TextEditingController();

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
    // Handle search logic here
    String searchText = _searchController.text.trim();
    // Implement your search functionality based on `searchText`
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
    );
  }
}
