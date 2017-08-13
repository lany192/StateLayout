# MultiStateView
这是一个Android自定义的多视图，包含加载中、错误、结果空和网络异常这4种视图，适合绝大多数app的界面切换

    compile 'com.lany:StateView:1.0.5'



    <com.lany.view.StateView
        android:id="@+id/stateView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:sv_viewState="content">
        
        <ListView
            android:id="@+id/list"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    
    </com.lany.view.StateView>


![image](https://github.com/lany192/MultiStateView/raw/master/Screenshot/c.png)
![image](https://github.com/lany192/MultiStateView/raw/master/Screenshot/b.png)
![image](https://github.com/lany192/MultiStateView/raw/master/Screenshot/a.png)
![image](https://github.com/lany192/MultiStateView/raw/master/Screenshot/d.png)
![image](https://github.com/lany192/MultiStateView/raw/master/Screenshot/e.png)
基于https://github.com/Kennyc1012/MultiStateView
