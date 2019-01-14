[![](https://jitpack.io/v/lany192/StateView.svg)](https://jitpack.io/#lany192/StateView)

# StateLayout

This is an Android custom view more, contained load, error, empty and abnormal network the four views as a result, for the vast majority of app interface switching


这是一个Android自定义的多视图切换类，包含加载中、错误、结果空、网络异常和内容这5种视图，适合绝大多数app的界面切换
## Gradle

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
        
    dependencies {
        implementation 'com.github.lany192:StateView:1.2.3'
    }
    
## Layout
    <com.lany.state.StateLayout
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/stateLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:emptyView="@layout/custom_empty_view"
        app:errorView="@layout/custom_error_view"
        app:loadingView="@layout/custom_loading_view"
        app:networkView="@layout/custom_network_view"
        app:viewState="content">

        <ListView
            android:id="@+id/list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:listitem="@android:layout/simple_list_item_1" />

    </com.lany.state.StateLayout>

##### 或者使用默认配置

    <com.lany.state.StateLayout
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/stateLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <ListView
            android:id="@+id/list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:listitem="@android:layout/simple_list_item_1" />

    </com.lany.state.StateLayout>
    
## Preview
![image](https://github.com/lany192/MultiStateView/raw/master/preview/video.gif)
