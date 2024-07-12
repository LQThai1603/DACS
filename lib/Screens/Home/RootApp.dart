import 'package:dacs/theme/colors.dart';
import 'package:dacs/theme/constant.dart';
import 'package:dacs/widgets/RootApp_widgets/bottombar_item.dart';
import 'package:flutter/material.dart';
import 'Forum.dart';
import 'posts.dart';
import 'profile.dart';
import 'home_screen.dart';

class RootApp extends StatefulWidget {
  const RootApp({Key? key}) : super(key: key);

  @override
  _RootAppState createState() => _RootAppState();
}

class _RootAppState extends State<RootApp> with TickerProviderStateMixin {
  int _activeTab = 0;
  final List barItems = [
    {
      "icon": "assets/home-border.svg",
      "active_icon": "assets/home.svg",
      "page": HomeScreen(),
    },
    {
      "icon": "assets/post-1.svg",
      "active_icon": "assets/post-1.svg",
      "page": Forum(),
    },
    {
      "icon": "assets/heart.svg",
      "active_icon": "assets/heart.svg",
      "page": Posts(),
    },
    {
      "icon": "assets/profile.svg",
      "active_icon": "assets/profile.svg",
      "page": Profile()
    },
  ];

//====== set animation=====
  late final AnimationController _controller = AnimationController(
    duration: const Duration(milliseconds: ANIMATED_BODY_MS),
    vsync: this,
  );
  late final Animation<double> _animation = CurvedAnimation(
    parent: _controller,
    curve: Curves.fastOutSlowIn,
  );

  @override
  void initState() {
    super.initState();
    _controller.forward();
  }

  @override
  void dispose() {
    _controller.stop();
    _controller.dispose();
    super.dispose();
  }

  _buildAnimatedPage(page) {
    return FadeTransition(child: page, opacity: _animation);
  }

  void onPageChanged(int index) {
    _controller.reset();
    setState(() {
      _activeTab = index;
    });
    _controller.forward();
  }

//====== end set animation=====

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColor.appBgColor,
      body: _buildPage(),
      floatingActionButton: _buildBottomBar(),
      floatingActionButtonLocation:
      FloatingActionButtonLocation.miniCenterDocked,
    );
  }

  Widget _buildPage() {
    return IndexedStack(
      index: _activeTab,
      children: List.generate(
        barItems.length,
            (index) => _buildAnimatedPage(barItems[index]["page"]),
      ),
    );
  }

  Widget _buildBottomBar() {
    return Container(
      height: 55,
      width: double.infinity,
      margin: EdgeInsets.symmetric(horizontal: 25, vertical: 10),
      decoration: BoxDecoration(
        color: AppColor.bottomBarColor,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: AppColor.shadowColor.withOpacity(0.1),
            blurRadius: 1,
            spreadRadius: 1,
            offset: Offset(0, 1),
          )
        ],
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: List.generate(
          barItems.length,
              (index) => BottomBarItem(
            _activeTab == index
                ? barItems[index]["active_icon"]
                : barItems[index]["icon"],
            isActive: _activeTab == index,
            activeColor: AppColor.primary,
            onTap: () => onPageChanged(index),
          ),
        ),
      ),
    );
  }
}
