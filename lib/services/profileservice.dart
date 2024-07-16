import 'package:dacs/models/profileModel.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class ProfileService with ChangeNotifier {
  ProfileModel? _profile;

  ProfileModel? get profile => _profile;

  Future<void> fetchProfile(String userName) async {
    final String apiUrl = 'http://192.168.100.107:8080/api/profile/$userName';

    try {
      final response = await http.get(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        _profile = ProfileModel.fromJson(jsonDecode(response.body));
        notifyListeners();
      } else {
        throw Exception('Failed to load profile: ${response.statusCode}');
      }
    } catch (e) {
      print('Error fetching profile: $e');
      throw Exception('Failed to connect to the server');
    }
  }
}
