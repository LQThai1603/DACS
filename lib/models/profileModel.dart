import 'dart:convert';

ProfileModel profileJson(String str) => ProfileModel.fromJson(json.decode(str));

String profileToJson(ProfileModel data) => json.encode(data.toJson());

class ProfileModel {
  final String userNameProfile;
  final String avatar;
  final String birthDay;
  final String name;
  final String phoneNumber;
  final String sex;

  ProfileModel({required this.userNameProfile, required this.avatar, required this.birthDay, required this.name,
    required this.phoneNumber, required this.sex});

  factory ProfileModel.fromJson(Map<String, dynamic> json) => ProfileModel(
      userNameProfile: json["userNameProfile"],
      avatar: json["avatar"],
      birthDay: json["birthDay"],
      name: json["name"],
      phoneNumber: json["phoneNumber"],
      sex: json["sex"],
  );

  Map<String, dynamic> toJson() => {
    "userNameProfile": userNameProfile,
    "avatar": avatar,
    "birthDay": birthDay,
    "name": name,
    "phoneNumber": phoneNumber,
    "sex": sex,
  };

  String get getUserNameProfile => userNameProfile;
  String get getAvatar => avatar;
  String get getBirthDay => birthDay;
  String get getName => name;
  String get getPhoneNumber => phoneNumber;
  String get getSex => sex;
}