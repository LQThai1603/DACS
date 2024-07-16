import 'dart:convert';

Postmodel postModelJson(String str) => Postmodel.fromJson(json.decode(str));

String postModelToJson(Postmodel data) => json.encode(data.toJson());

class Postmodel {
  final int id;
  final String avatar;
  final String content;
  final String image;
  final String time;
  final String title;
  final String userNameProfile;
  Postmodel(
      {required this.id,
      required this.avatar,
      required this.content,
      required this.image,
      required this.time,
      required this.title,
      required this.userNameProfile});

  factory Postmodel.fromJson(Map<String, dynamic> json) => Postmodel(
      id: json["id"],
      avatar: json["avatar"],
      content: json["content"],
      image: json["image"],
      time: json["time"],
      title: json["title"],
      userNameProfile: json["userNameProfile"]);

  Map<String, dynamic> toJson() => {
        "id": id,
        "avatar": avatar,
        "content": content,
        "image": image,
        "time": time,
        "title": title,
        "userNameProfile": userNameProfile,
      };

  int get getId => id;
  String get getAvatar => avatar;
  String get getContent => content;
  String get getImage => image;
  String get getTime => time;
  String get getTitle => title;
  String get getUserNameProfile => userNameProfile;

}
