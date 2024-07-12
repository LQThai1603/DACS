class ApiEndPoints {
  static final baseUrl = 'http://restapi.adequateshop.com/api';
  static _AuthEndPoints authEndpoints = _AuthEndPoints();
}

class  _AuthEndPoints {
  final String registerEmail = 'authaccount/registration';
  final String loginEmail = 'authaccount/login';
}