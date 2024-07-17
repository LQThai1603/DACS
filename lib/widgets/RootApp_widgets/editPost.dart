import 'dart:convert';
import 'dart:io';

import 'package:dacs/Screens/Home/Forum.dart';
import 'package:dacs/models/postModel.dart';
import 'package:dacs/models/profileModel.dart';
import 'package:dacs/widgets/RootApp_widgets/information.dart';
import 'package:dacs/widgets/RootApp_widgets/wallPerson.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:path/path.dart';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart';

class Editpost extends StatefulWidget {
  final String userName;
  final String postId;

  const Editpost({Key? key, required this.userName, required this.postId})
      : super(key: key);

  @override
  _EditPostState createState() => _EditPostState();
}

class _EditPostState extends State<Editpost> {
  File? imageFile;
  late final TextEditingController _titleTextEditingController;
  late final TextEditingController _contentTextEditingController;

  late Future<ProfileModel> futureProfile;
  late Future<Postmodel> futurePost;

  @override
  void initState() {
    _titleTextEditingController = TextEditingController();
    _contentTextEditingController = TextEditingController();
    futureProfile = _fetchProfile();
    futurePost = _fetchPost();

    super.initState();
  }

  @override
  void dispose() {
    _titleTextEditingController.dispose();
    _contentTextEditingController.dispose();
    super.dispose();
  }

  Future<void> editPost(String title, String content, File? imageFile,
      BuildContext context) async {
    final url = 'http://192.168.100.107:8080/api/edit/post/${widget.postId}';

    try {
      Dio dio = Dio();
      ProfileModel profile = await futureProfile;

      FormData formData = FormData.fromMap({
        'title': title,
        'content': content,
        'userNameProfile': widget.userName,
        'avatar': profile.avatar,
        'image': imageFile != null
            ? await MultipartFile.fromFile(imageFile.path,
            filename: basename(imageFile.path))
            : null,
      });

      var response = await dio.put(
        url,
        data: formData,
        options: Options(
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        ),
      );

      if (response.statusCode == 200) {
        print('Post updated successfully');
        print('Response: ${response.data}');
        ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Post updated successfully')));
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (context) => Wallperson(userName: widget.userName),
          ),
        );
      } else {
        print('Failed to update post. Status code: ${response.statusCode}');
        print('Response: ${response.data}');
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content:
          Text('Failed to update post. Status code: ${response.statusCode}'),
        ));
      }
    } catch (e) {
      print('Error updating post: $e');
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text('Error updating post: $e')));
    }
  }

  Future<Postmodel> _fetchPost() async {
    final response = await http.get(
        Uri.parse('http://192.168.100.107:8080/api/show/post/${widget.postId}'));

    if (response.statusCode == 200) {
      try {
        Map<String, dynamic> jsonData = json.decode(response.body);
        Postmodel post = Postmodel.fromJson(jsonData);
        _titleTextEditingController.text = post.title;
        _contentTextEditingController.text = post.content;
        return post;
      } catch (e) {
        print('Error parsing JSON: $e');
        throw Exception('Failed to parse post');
      }
    } else {
      throw Exception('Failed to load post');
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

  Future<File?> pickImage() async {
    final picker = ImagePicker();
    final pickedFile = await picker.pickImage(source: ImageSource.gallery);

    if (pickedFile != null) {
      return File(pickedFile.path);
    } else {
      print('No image selected');
      return null;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: Text('Sửa bài viết'),
        centerTitle: true,
        backgroundColor: Colors.white,
        actions: [
          Padding(
            padding: const EdgeInsets.only(right: 16.0),
            child: ElevatedButton(
              style: ElevatedButton.styleFrom(
                minimumSize: Size.zero,
                padding: EdgeInsets.symmetric(horizontal: 10, vertical: 8),
                backgroundColor: Colors.blue,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
              onPressed: () async {
                ProfileModel profile = await futureProfile;
                await editPost(
                  _titleTextEditingController.text,
                  _contentTextEditingController.text,
                  imageFile,
                  context,
                );
              },
              child: const Text(
                'Đăng',
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 16,
                    fontWeight: FontWeight.bold),
              ),
            ),
          ),
        ],
      ),
      body: FutureBuilder<Postmodel>(
        future: futurePost,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData) {
            final post = snapshot.data!;
            return Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Row(
                    children: [
                      CircleAvatar(
                        radius: 20,
                        backgroundColor: Colors.grey[200],
                        backgroundImage: NetworkImage(
                            'http://192.168.100.107:8080/api/show/avatar${post.avatar}'),
                      ),
                      const SizedBox(width: 8),
                      Text(post.userNameProfile,
                          style: TextStyle(color: Colors.black)),
                    ],
                  ),
                  const SizedBox(height: 10),
                  TextField(
                    controller: _titleTextEditingController,
                    decoration: InputDecoration(
                      hintText: 'Tiêu đề của bạn là gì?',
                      border: InputBorder.none,
                    ),
                    keyboardType: TextInputType.multiline,
                    minLines: 1,
                    maxLines: 10,
                  ),
                  const SizedBox(height: 10),
                  TextField(
                    controller: _contentTextEditingController,
                    decoration: InputDecoration(
                      hintText: 'Bạn đang nghĩ gì?',
                      border: InputBorder.none,
                    ),
                    keyboardType: TextInputType.multiline,
                    minLines: 1,
                    maxLines: 10,
                  ),
                  SizedBox(height: 16),
                  Image.network(
                      'http://192.168.100.107:8080/api/show/postImage${post.image}',),
                  SizedBox(height: 16),
                  PickFileWidget(
                    pickImage: () async {
                      imageFile = await pickImage();
                      setState(() {});
                    },
                  ),
                ],
              ),
            );
          } else {
            return Center(child: Text('No post data'));
          }
        },
      ),
    );
  }
}

class PickFileWidget extends StatelessWidget {
  const PickFileWidget({Key? key, required this.pickImage}) : super(key: key);

  final VoidCallback pickImage;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        ElevatedButton.icon(
          style: ElevatedButton.styleFrom(backgroundColor: Colors.white),
          onPressed: pickImage,
          icon: Icon(
            Icons.image,
            color: Colors.green,
          ),
          label: Text(
            'Chọn ảnh',
            style: TextStyle(color: Colors.grey),
          ),
        ),
      ],
    );
  }
}
