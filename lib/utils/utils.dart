import 'dart:io';

import 'package:image_picker/image_picker.dart';

Future<File?> pickImage() async {
  try {
    File? image;
    final picker = ImagePicker();
    final file = await picker.pickImage(
      source: ImageSource.gallery,
      maxHeight: 720,
      maxWidth: 720,
    );

    if (file != null) {
      image = File(file.path);
    }
    return image;
  } catch (e) {
    print('Lỗi khi chọn ảnh: $e');
    return null;
  }
}
