import 'dart:convert';

Commentmodel commentModelJson(String str) =>
    Commentmodel.fromJson(json.decode(str));

String commentModelToJson(Commentmodel data) => json.encode(data.toJson());

class Commentmodel {
  final int id;
  final String avatar;
  final String content;
  final int idPost;
  final String time;
  final String userNameProfile;

  Commentmodel(
      {required this.id,
      required this.avatar,
      required this.content,
      required this.idPost,
      required this.time,
      required this.userNameProfile});

  factory Commentmodel.fromJson(Map<String, dynamic> json) => Commentmodel(
      id: json["id"],
      avatar: json["avatar"],
      content: json["content"],
      idPost: json["idPost"],
      time: json["time"],
      userNameProfile: json["userNameProfile"]);

  Map<String, dynamic> toJson() => {
        "id": id,
        "avatar": avatar,
        "content": content,
        "idPost": idPost,
        "time": time,
        "userNameProfile": userNameProfile,
      };

  int get getId => id;

  String get getAvatar => avatar;

  String get getContent => content;

  int get getIdPost => idPost;

  String get getTime => time;

  String get getUserNameProfile => userNameProfile;
}
