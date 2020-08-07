### FireworkSDK
FireworkSDK 是一個用於整合Firework短視頻於您的Android應用程式的應用庫。

### FireworkSDK 範例展示
下載連結: https://github.com/loopsocial/firework_sdk_official/blob/master/FireworkDemo.apk

### 前置需求
在您開始整合FireworkSDK至您的應用程式前，你必須先向Firework團隊註冊的應用程式，以取得您的專屬app_id。步驟如下:

- [X] 提供您Android專案的applicationId或專案名稱給Firework團隊。如果您的applicationId和專案名稱是不同的，請以applicationId為主。

- [X] Firework團隊在收到您的申請需求後，將會發送一組您的app_id.

[註]∶ 此app_id將用於您的應用程式與後台之間的認證，如果您直接套用於非註冊的applicationId或專案名稱，此app_id將無法正常運作。
 

### 如何套用至您的專案? 

版本通知 https://github.com/loopsocial/firework_sdk_official/blob/master/RELEASENOTES.MD
 
- [X] 新增如下(1) - (5)於AndroidManifest.xml  
		<application>	
		....
		....
		
			//(1) 新增播放頁面的 Activity
		 	<activity android:name="com.loopnow.fireworklibrary.PlaybackActivity"
		 		android:screenOrientation="portrait"
		 		android:theme="@style/FireworkSDK.NoActionBar.FullScreen"
		 	/>
			
			//(2) 新增開啟廣告頁面的web Activity 
			<activity android:name="com.loopnow.fireworklibrary.views.FireworkWebClientActivity"
            			android:theme="@style/AppTheme.NoActionBar.FullScreen"
            		/>
			
			//(3) 用於預存影片的Service 
        		<service android:name="com.loopnow.fireworklibrary.views.CacheService" />

			//(4) 新增您的Firework app_id
			<meta-data android:name="Firework:AppID" android:value="{app_id provided to you}" />
        
			//(5) 為了優化您在廣告項目上的內容及營利，將需此設定取得您應用程試的advertising_id。
			<meta-data
            			android:name="com.google.android.gms.ads.AD_MANAGER_APP"
            			android:value="true"/>
	    
	    
		</application>
    
- [X] 新增FireworkSDK至應用程式的主要build.gradle

		dependencies {
			------ other dependencies 
			------ 
			------
			implementation 'com.github.loopsocial:firework_sdk:{latest_version}' 
			
			//版本號參照 to https://github.com/loopsocial/firework_sdk_official/blob/master/RELEASENOTES.MD

		}

		android {
			.....
			.....
		
			dataBinding {
				enabled = true
			}

			compileOptions {
				sourceCompatibility 1.8
				targetCompatibility 1.8
	     		}
		}
	
- [X] 新增jitpack.io至app的build.gradle
	
		allprojects {
			repositories {
			
				--
				---
				--- 
				maven { url 'https://jitpack.io' }
			}
		}
	
- [X] 新增如下代碼於 proguard-rules.pro 
		
		-keepclassmembers class com.loopnow.fireworklibrary.** { <fields>; }


  
### 整合影片UI至您應用程式 

您有兩種方式可以整合Firework video到應用程式

1. VideoFeedView: 提供影片縮圖清單，將影片清單以view的方式整合至您的layout，並且透過點選縮圖後您可以開始播放器觀賞影片。VideoFeedView提供三種配置:

- 垂直 vertical
- 水平 horizontal
- 網格 grid

依據您的需求，可參照以下VideoFeedView範例在layout中做調整: 

			<com.loopnow.fireworklibrary.VideoFeedView
	   			android:name="com.loopnow.fireworklibrary.views.VideoFeedFragment"
	   			android:layout_width="{寬}"
	   			android:layout_height="{高}"
	   			app:showTitle="{true / false}"
	   			app:feedLayout="{grid | horizontal | vertical}"
	  			app:columns="{如果使用grid，加入所需列數}">
			/>
			
- {寬} : 必須定義所需VideoFeedView的寬。
- {高} : 必須定義所需VideoFeedView的高。

- app:feedLayout={grid | horizontal | vertical} : 定義縮圖顯示的樣示。支援的參數如下
		
1. grid: 影片縮圖將以 ```<欄> x <列>``` 的格式顯示，支援垂直捲動。如果沒有指定```app:columns```，預設值為2。		
<img src="screenshots/grid.jpg"  width="270" height="480"> <img src="screenshots/grid_with_title.jpg"  width="270" height="480">
			
			
2. horizontal: 影片縮圖將以水平方式顯示，支援水平捲動。
		  
<img src="screenshots/Horizontal_video_list.png"  width="270" height="480">
	
3. vertical : 影片縮圖將以垂直方式顯示，支援垂直捲動。
		
<img src="screenshots/vertical.jpg"  width="270" height="480"> <img src="screenshots/vertical_with_title.jpg"  width="270" height="480">
	
若您選擇vertical或grid模式,建議使用 layout_height="match_parent"。反之，若是horizontal模式，則提供%或dp。
	       
	       如下: 
	       
	       1. feedLayout="vertical" or feedLaout="grid"
	          layout_height="match_parent"
		  
	       2. feedLayout="horizontal" 
	       	  layout_height="200dp" 
		  
		  or 如果使用 ConstraintLayout
		  
		  layout_height="0dp" 
		  app:layout_constraintHeight_default="percent"
                  app:layout_constraintHeight_percent="0.40"
		 
3. app:columns: 此屬性只有在使用grid配置時才有作用，預設值為2。

4. app:showTitle={true|false} : 此為非必要屬性，用於顯示影片標題於縮圖上，可根據您的需求選擇開啟標題(true)或關閉標題(false)。



2. FireworkPlayerFragment : 

如果您不需要影片清單，只需要播放器全屏播放影片，讓用戶端可以直接透過左划或右划的手勢瀏覽影片。您可以選擇以FireworkPlayerFragment的方式，直接整合影片播放器至您的應用程式。

		<fragment android:layout_width="match_parent"
            		android:layout_height="match_parent"
            		android:name="com.loopnow.fireworklibrary.views.FireworkPlayerFragment"
            		app:appid="{your_app_id}"
            		android:id="@+id/{your_fragment_id}"
            		android:tag="player_fragment"
           		/>
