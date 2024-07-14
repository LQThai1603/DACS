import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'package:path/path.dart';

class ProfileScreen extends StatefulWidget {
  final String userName;

  ProfileScreen({required this.userName});

  @override
  _ProfileScreenState createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  final _formKey = GlobalKey<FormState>();
  TextEditingController _nameController = TextEditingController();
  TextEditingController _phoneController = TextEditingController();
  TextEditingController _birthdayController = TextEditingController();
  TextEditingController _sexController = TextEditingController();
  File? _avatar;

  Future<void> _updateProfile() async {
    if (_formKey.currentState!.validate()) {
      var uri = Uri.parse('http://192.168.144.1:8080/api/edit/profile/${widget.userName}');
      var request = http.MultipartRequest('POST', uri);

      request.headers['Content-Type'] = 'multipart/form-data';

      request.fields['userNameProfile'] = widget.userName;
      request.fields['name'] = _nameController.text;
      request.fields['phoneNumber'] = _phoneController.text;
      request.fields['birthDay'] = _birthdayController.text;
      request.fields['sex'] = _sexController.text;

      if (_avatar != null) {
        var stream = http.ByteStream(_avatar!.openRead());
        var length = await _avatar!.length();

        request.files.add(http.MultipartFile(
          'avatar',
          stream,
          length,
          filename: basename(_avatar!.path),
        ));
      }

      print('Sending request: ${request.fields}');
      if (_avatar != null) {
        print('Avatar path: ${_avatar!.path}');
      }

      var response = await request.send();

      print('Response status: ${response.statusCode}');
      var responseBody = await response.stream.bytesToString();
      print('Response body: $responseBody');

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context as BuildContext).showSnackBar(SnackBar(content: Text('Profile updated successfully')));
      } else {
        throw Exception('Failed to update profile');
      }
    }
  }

  Future<void> _pickAvatar() async {
    final pickedFile = await ImagePicker().getImage(source: ImageSource.gallery);

    setState(() {
      if (pickedFile != null) {
        _avatar = File(pickedFile.path);
      }
    });
  }

  @override
  void initState() {
    super.initState();
    _fetchProfile();
  }

  Future<void> _fetchProfile() async {
    final response = await http.get(Uri.parse('http://192.168.144.1:8080/api/show/profile/${widget.userName}'));

    if (response.statusCode == 200) {
      final profile = jsonDecode(response.body);
      setState(() {
        _nameController.text = profile['name'];
        _phoneController.text = profile['phoneNumber'];
        _birthdayController.text = profile['birthDay'];
        _sexController.text = profile['sex'];
      });
    } else {
      throw Exception('Failed to load profile');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Profile'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nameController,
                decoration: InputDecoration(labelText: 'Name'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter your name';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _phoneController,
                decoration: InputDecoration(labelText: 'Phone Number'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter your phone number';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _birthdayController,
                decoration: InputDecoration(labelText: 'Birthday'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter your birthday';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _sexController,
                decoration: InputDecoration(labelText: 'Sex'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter your sex';
                  }
                  return null;
                },
              ),
              SizedBox(height: 20),
              _avatar != null
                  ? Image.file(_avatar!)
                  : IconButton(
                icon: Icon(Icons.camera_alt),
                onPressed: _pickAvatar,
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: _updateProfile,
                child: Text('Update Profile'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
