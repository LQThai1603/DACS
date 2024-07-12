import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'home_screen.dart';
import 'register_screen.dart';  // Import màn hình đăng ký mới

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  String _message = '';

  Future<void> loginUser() async {
    final String apiUrl = 'http://192.168.144.1:8080/api/show/account/${_usernameController.text}';

    try {
      final response = await http.get(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );
      print('Response status: ${response.statusCode}');
      print('Response body: ${response.body}');

      if (response.statusCode == 200) {
        final user = jsonDecode(response.body);
        print('User: $user');

        // Kiểm tra xem đăng nhập thành công hay không dựa vào phản hồi từ API
        if (user['passWord'] == _passwordController.text) {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => HomeScreen(userName: user['userName'])),
          );
        } else {
          setState(() {
            _message = 'Invalid username or password';
          });
        }
      } else {
        setState(() {
          _message = 'Failed to login';
        });
      }
    } catch (e) {
      print('Error logging in: $e');
      setState(() {
        _message = 'Failed to connect to server';
      });
    }
  }

  void _trySubmit() {
    final isValid = _formKey.currentState!.validate();
    FocusScope.of(context).unfocus();

    if (isValid) {
      _formKey.currentState!.save();
      loginUser();
    }
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
                    )),
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
                                color: Theme.of(context).primaryColor, fontSize: 32, fontWeight: FontWeight.w500),
                          ),
                          const Text(
                            'Please login with your information',
                            style: TextStyle(color: Colors.grey),
                          ),
                          const SizedBox(height: 10,),
                          Container(
                            padding: const EdgeInsets.symmetric(vertical: 2.0, horizontal: 30.0),
                            margin: const EdgeInsets.symmetric(vertical: 10.0),
                            decoration: BoxDecoration(
                                color: const Color(0xFFedf0f8),
                                borderRadius: BorderRadius.circular(30)),
                            child: TextFormField(
                              key: const ValueKey('username'),
                              validator: (value) {
                                if (value == null || value.isEmpty) {
                                  return 'Please enter your username';
                                }
                                return null;
                              },
                              decoration: const InputDecoration(
                                border: InputBorder.none,
                                hintText: "Username",
                                hintStyle: TextStyle(
                                    color: Color(0xFFb2b7bf), fontSize: 16.0),
                                labelText: 'Username',
                              ),
                              onSaved: (value) {
                                _usernameController.text = value!;
                              },
                            ),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(vertical: 2.0, horizontal: 30.0),
                            margin: const EdgeInsets.symmetric(vertical: 10.0),
                            decoration: BoxDecoration(
                                color: const Color(0xFFedf0f8),
                                borderRadius: BorderRadius.circular(30)),
                            child: TextFormField(
                              key: const ValueKey('password'),
                              validator: (value) {
                                if (value == null || value.isEmpty || value.length < 7) {
                                  return 'Password must be at least 7 characters long';
                                }
                                return null;
                              },
                              decoration: const InputDecoration(
                                border: InputBorder.none,
                                hintText: "Password",
                                hintStyle: TextStyle(
                                    color: Color(0xFFb2b7bf), fontSize: 16.0),
                                labelText: 'Password',
                              ),
                              obscureText: true,
                              onSaved: (value) {
                                _passwordController.text = value!;
                              },
                            ),
                          ),
                          const SizedBox(
                            height: 12,
                          ),
                          Container(
                            width: MediaQuery.of(context).size.width,
                            height: 50.0,
                            decoration: BoxDecoration(
                              color: Color(0xFF273671),
                              borderRadius: BorderRadius.circular(30),
                            ),
                            child: ElevatedButton(
                              onPressed: _trySubmit,
                              style: ElevatedButton.styleFrom(
                                primary: Theme.of(context).primaryColor,
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
                                minimumSize: Size(double.infinity, 50.0),
                              ),
                              child: const Text(
                                'Login',
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
                            style: TextStyle(color: Colors.red),
                          ),
                          const SizedBox(height: 20),
                          TextButton(
                            onPressed: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(builder: (context) => RegisterScreen()),
                              );
                            },
                            child: const Text(
                              'Don\'t have an account? Sign up here',
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
