import 'dart:io';

import 'package:dacs/models/profileModel.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class InformationUser extends StatefulWidget {
  final String userName;

  const InformationUser({super.key, required this.userName});

  @override
  State<InformationUser> createState() => _InformationUserState();
}

class _InformationUserState extends State<InformationUser> {
  final _formKey = GlobalKey<FormState>();
  File? _avatar;

  late Future<ProfileModel> futureProfile;
  TextEditingController _nameController = TextEditingController();
  TextEditingController _phoneNumberController = TextEditingController();
  TextEditingController _birthdayController = TextEditingController();
  TextEditingController _sexController = TextEditingController();

  Future<void> _pickAvatar() async {
    final pickedFile =
        await ImagePicker().pickImage(source: ImageSource.gallery);

    setState(() {
      if (pickedFile != null) {
        _avatar = File(pickedFile.path);
      }
    });
  }

  @override
  void initState() {
    super.initState();
    futureProfile = _fetchProfile();
  }

  Future<ProfileModel> _fetchProfile() async {
    final response = await http.get(
      Uri.parse(
          'http://192.168.100.107:8080/api/show/profile/${widget.userName}'),
    );

    if (response.statusCode == 200) {
      try {
        Map<String, dynamic> jsonData = json.decode(response.body);
        ProfileModel profile = ProfileModel.fromJson(jsonData);

        // Gán giá trị vào các TextEditingController
        _nameController.text = profile.name ?? '';
        _phoneNumberController.text = profile.phoneNumber ?? '';
        _birthdayController.text = profile.birthDay ?? '';
        _sexController.text = profile.sex ?? '';

        return profile;
      } catch (e) {
        print('Error parsing JSON: $e');
        throw Exception('Failed to parse profile');
      }
    } else {
      throw Exception('Failed to load profile');
    }
  }

  Future<void> _updateProfile(BuildContext context) async {
    if (_formKey.currentState!.validate()) {
      final uri = Uri.parse(
          'http://192.168.100.107:8080/api/edit/profile/${widget.userName}');
      var request = http.MultipartRequest('PUT', uri);

      if (_avatar != null) {
        request.files
            .add(await http.MultipartFile.fromPath('avatar', _avatar!.path));
      }

      request.fields['name'] = _nameController.text;
      request.fields['phoneNumber'] = _phoneNumberController.text;
      request.fields['birthDay'] = _birthdayController.text;
      request.fields['sex'] = _sexController.text;

      var response = await request.send();

      if (response.statusCode == 200) {
        final responseBody = await response.stream.bytesToString();
        final updatedProfile = ProfileModel.fromJson(json.decode(responseBody));
        setState(() {
          futureProfile = Future.value(updatedProfile);
        });
        _showSnackBar(context, 'Profile updated successfully');
      } else {
        _showSnackBar(context, 'Failed to update profile');
      }
    }
  }

  void _showSnackBar(BuildContext context, String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
    );
  }

  Future<void> _selectDate(BuildContext context) async {
    DateTime? pickedDate = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1900),
      lastDate: DateTime(2100),
    );

    if (pickedDate != null) {
      setState(() {
        _birthdayController.text = DateFormat('yyyy-MM-dd').format(pickedDate);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Thông tin cá nhân',
          style: TextStyle(color: Colors.white),
        ),
        backgroundColor: Colors.blue,
        iconTheme: IconThemeData(color: Colors.white),
      ),
      body: FutureBuilder<ProfileModel>(
        future: futureProfile,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            final profile = snapshot.data!;

            return Padding(
              padding: const EdgeInsets.all(16.0),
              child: Form(
                key: _formKey,
                child: ListView(
                  children: [
                    CircleAvatar(
                      radius: 100,
                      backgroundImage: NetworkImage(
                          'http://192.168.100.107:8080/api/show/avatar${profile.avatar}'),
                    ),
                    SizedBox(height: 5,),
                    InkWell(
                      onTap: _pickAvatar,
                      child: CircleAvatar(
                        radius: 20,
                        backgroundColor: Colors.blue,
                        child: Icon(
                          Icons.camera_alt,
                          color: Colors.white,
                        ),
                      ),
                    ),
                    SizedBox(height: 20),
                    TextFormField(
                      controller: _nameController,
                      decoration: InputDecoration(
                        labelText: 'Name',
                        border: OutlineInputBorder(),
                        filled: true,
                        fillColor: Colors.grey[200],
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Please enter your name';
                        }
                        return null;
                      },
                    ),
                    SizedBox(height: 10),
                    TextFormField(
                      controller: _phoneNumberController,
                      decoration: InputDecoration(
                        labelText: 'Phone Number',
                        border: OutlineInputBorder(),
                        filled: true,
                        fillColor: Colors.grey[200],
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Please enter your phone number';
                        }
                        return null;
                      },
                    ),
                    SizedBox(height: 10),
                    GestureDetector(
                      onTap: () => _selectDate(context),
                      child: AbsorbPointer(
                        child: TextFormField(
                          controller: _birthdayController,
                          decoration: InputDecoration(
                            labelText: 'Birthday',
                            border: OutlineInputBorder(),
                            filled: true,
                            fillColor: Colors.grey[200],
                          ),
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please enter your birthday';
                            }
                            return null;
                          },
                        ),
                      ),
                    ),
                    SizedBox(height: 10),
                    DropdownButtonFormField<String>(
                      value: _sexController.text.isNotEmpty
                          ? _sexController.text
                          : null,
                      items: <String>['Male', 'Female', 'Other']
                          .map((String value) {
                        return DropdownMenuItem<String>(
                          value: value,
                          child: Text(value),
                        );
                      }).toList(),
                      onChanged: (newValue) {
                        setState(() {
                          _sexController.text = newValue!;
                        });
                      },
                      decoration: InputDecoration(
                        labelText: 'Sex',
                        border: OutlineInputBorder(),
                        filled: true,
                        fillColor: Colors.grey[200],
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Please select your sex';
                        }
                        return null;
                      },
                    ),
                    SizedBox(height: 20),
                    Builder(builder: (BuildContext newContext) {
                      return ElevatedButton(
                        onPressed: () {
                          _updateProfile(newContext);
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.blue,
                          textStyle: TextStyle(color: Colors.white),
                        ),
                        child: Text('Update Profile',
                            style: TextStyle(color: Colors.white)),
                      );
                    }),
                  ],
                ),
              ),
            );
          } else if (snapshot.hasError) {
            return Center(
              child: Text('Error loading profile: ${snapshot.error}'),
            );
          }
          return Center(child: CircularProgressIndicator());
        },
      ),
    );
  }
}
