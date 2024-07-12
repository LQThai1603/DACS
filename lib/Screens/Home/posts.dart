import 'package:flutter/material.dart';

class Posts extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Posts'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Welcome, !',
              style: TextStyle(fontSize: 24),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                // Log out logic if needed
                Navigator.pop(context); // Go back to previous screen
              },
              child: Text('Log out'),
            ),
          ],
        ),
      ),
    );
  }
}
