
import 'package:dacs/Screens/Auth/login_screen.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class RegisterScreen extends StatefulWidget {
  @override
  _RegisterScreenState createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _confirmPasswordController = TextEditingController();
  String _message = '';

  Future<void> _registerUser() async {
    final String apiUrl = 'http://192.168.100.107:8080/api/create/account';

    if (!_formKey.currentState!.validate()) {
      return;
    }

    final response = await http.post(
      Uri.parse(apiUrl),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'userName': _usernameController.text.trim(),
        'passWord': _passwordController.text.trim(),
      }),
    );

    if (response.statusCode == 201) {
      setState(() {
        _message = 'Account created successfully';
      });
    } else {
      setState(() {
        _message = 'Failed to create account';
      });
    }
  }

  String? _validateUsername(String? value) {
    final regex = RegExp(r'^[a-zA-Z]+$');
    if (value == null || value.isEmpty) {
      return 'Please enter a username';
    } else if (!regex.hasMatch(value)) {
      return 'Username cannot contain special characters or numbers';
    }
    return null;
  }


  String? _validatePassword(String? value) {
    if (value == null || value.isEmpty || value.length < 10) {
      return 'Password must be at least 10 characters long';
    }
    return null;
  }

  String? _validateConfirmPassword(String? value) {
    if (value == null || value != _passwordController.text) {
      return 'Passwords do not match';
    }
    return null;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SingleChildScrollView(
        child: Column(
          children: [
            Stack(
              children: [
                Container(
                  width: MediaQuery.of(context).size.width,
                  height: MediaQuery.of(context).size.width * 0.8,
                  child: Image.asset(
                    "assets/suckhoe.jpg",
                    fit: BoxFit.cover,
                  ),
                ),
                Container(
                  margin: const EdgeInsets.only(top: 260),
                  decoration: const BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.only(
                      topLeft: Radius.circular(30),
                      topRight: Radius.circular(30),
                    ),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.only(top: 30, left: 20.0, right: 20.0),
                    child: Form(
                      key: _formKey,
                      child: Column(
                        children: <Widget>[
                          Text(
                            "Welcome",
                            style: TextStyle(
                              color: Theme.of(context).primaryColor,
                              fontSize: 32,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                          const Text(
                            'Please sign up with your information',
                            style: TextStyle(color: Colors.grey),
                          ),
                          const SizedBox(height: 10),
                          Container(
                            padding: const EdgeInsets.symmetric(vertical: 2.0, horizontal: 30.0),
                            margin: const EdgeInsets.symmetric(vertical: 10.0),
                            decoration: BoxDecoration(
                              color: const Color(0xFFedf0f8),
                              borderRadius: BorderRadius.circular(30),
                            ),
                            child: TextFormField(
                              key: const ValueKey('username'),
                              validator: _validateUsername,
                              decoration: const InputDecoration(
                                border: InputBorder.none,
                                hintText: "Username",
                                hintStyle: TextStyle(
                                  color: Color(0xFFb2b7bf),
                                  fontSize: 16.0,
                                ),
                                labelText: 'Username',
                              ),
                              controller: _usernameController,
                            ),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(vertical: 2.0, horizontal: 30.0),
                            margin: const EdgeInsets.symmetric(vertical: 10.0),
                            decoration: BoxDecoration(
                              color: const Color(0xFFedf0f8),
                              borderRadius: BorderRadius.circular(30),
                            ),
                            child: TextFormField(
                              key: const ValueKey('password'),
                              validator: _validatePassword,
                              decoration: const InputDecoration(
                                border: InputBorder.none,
                                hintText: "Password",
                                hintStyle: TextStyle(
                                  color: Color(0xFFb2b7bf),
                                  fontSize: 16.0,
                                ),
                                labelText: 'Password',
                              ),
                              obscureText: true,
                              controller: _passwordController,
                            ),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(vertical: 2.0, horizontal: 30.0),
                            margin: const EdgeInsets.symmetric(vertical: 10.0),
                            decoration: BoxDecoration(
                              color: const Color(0xFFedf0f8),
                              borderRadius: BorderRadius.circular(30),
                            ),
                            child: TextFormField(
                              key: const ValueKey('confirmPassword'),
                              validator: _validateConfirmPassword,
                              decoration: const InputDecoration(
                                border: InputBorder.none,
                                hintText: "Confirm Password",
                                hintStyle: TextStyle(
                                  color: Color(0xFFb2b7bf),
                                  fontSize: 16.0,
                                ),
                                labelText: 'Confirm Password',
                              ),
                              obscureText: true,
                              controller: _confirmPasswordController,
                            ),
                          ),
                          const SizedBox(height: 12),
                          Container(
                            width: MediaQuery.of(context).size.width,
                            height: 50.0,
                            decoration: BoxDecoration(
                              color: Color(0xFF273671),
                              borderRadius: BorderRadius.circular(30),
                            ),
                            child: ElevatedButton(
                              onPressed: _registerUser,
                              style: ElevatedButton.styleFrom(
                                backgroundColor: Theme.of(context).primaryColor,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(30),
                                ),
                                minimumSize: Size(double.infinity, 50.0),
                              ),
                              child: const Text(
                                'Sign Up',
                                style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 22.0,
                                  fontWeight: FontWeight.w500,
                                ),
                              ),
                            ),
                          ),
                          const SizedBox(height: 20),
                          Text(
                            _message,
                            style: TextStyle(color: Colors.green),
                          ),
                          const SizedBox(height: 20),
                          TextButton(
                            onPressed: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(builder: (context) => LoginScreen()),
                              );
                            },
                            child: const Text(
                              'You already have an account! Sign in here',
                              style: TextStyle(color: Colors.blue),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
