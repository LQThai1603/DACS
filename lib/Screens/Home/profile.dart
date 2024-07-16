import 'dart:convert';
import 'dart:io';
import 'package:dacs/Screens/Auth/login_screen.dart';
import 'package:dacs/widgets/RootApp_widgets/information.dart';
import 'package:dacs/widgets/RootApp_widgets/wallPerson.dart';
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
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Profile',
          style: TextStyle(color: Colors.white),
        ),
        automaticallyImplyLeading: false,
        backgroundColor: Colors.blue,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Container(

              width: double.maxFinite,
              child: ElevatedButton.icon(
                onPressed: () {
                  Navigator.push(context, MaterialPageRoute(builder: (context) => Wallperson(userName: widget.userName),),);
                },
                label: Text('Trang cá nhân', style: TextStyle(color: Colors.black),),
                icon: Icon(Icons.person, color: Colors.grey,),
              ),
            ),
            Container(
              width: double.maxFinite,
              child: ElevatedButton.icon(
                onPressed: () {
                  Navigator.push(context, MaterialPageRoute(builder: (context) => InformationUser(userName: widget.userName),),);
                },
                label: Text('Thông tin cá nhân', style: TextStyle(color: Colors.black),),
                icon: Icon(Icons.account_box, color: Colors.grey),
              ),
            ),
            Container(
              width: double.maxFinite,
              child: ElevatedButton.icon(
                onPressed: () {
                  Navigator.pop(context, MaterialPageRoute(builder: (context) => LoginScreen(),),);
                },
                label: Text('Đăng xuất', style: TextStyle(color: Colors.black),),
                icon: Icon(Icons.logout, color: Colors.red,),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
