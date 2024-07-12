import 'package:flutter/material.dart';

class HomeScreen extends StatelessWidget {
  final String userName;

  HomeScreen({required this.userName});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Home'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Welcome, $userName!',
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
