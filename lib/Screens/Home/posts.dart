import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class Posts extends StatelessWidget {
  final String time = DateFormat('yyyy-MM-dd – kk:mm').format(DateTime.now());
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Thông báo', style: TextStyle(color: Colors.white),),
        centerTitle: true,
        backgroundColor: Colors.blue,
        automaticallyImplyLeading: false,
      ),
      body: Container(
        child: Column(
          children: [
            Container(
              padding: EdgeInsets.all(10),
              color: Colors.grey[200],
              child: Row(
                children: [
                  CircleAvatar(
                    backgroundImage: AssetImage('assets/imagePerson.jpg'),
                  ),
                  const SizedBox(width: 8,),
                  Expanded(
                    child: Text(
                      'đã bình luận vào bài viết của bạn.'
                    ),
                  ),
                  Text(time),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
