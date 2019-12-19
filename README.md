# FireworkSDK
FireworkSDK is a library to integrate video feeds from ```Firework - a short form video platform``` into your Android application. 

# FireworkSDK Demo 
https://github.com/loopsocial/firework_sdk_official/blob/master/FireworkDemo.apk

# Prerequisites 
To integrate FireworkSDK into your applicaiton, you have to register your application with Firework platform. You have to provide <package_name> of your application and it must be the same as package name in AndroidManifest.xml. Once you register your application, you will be provided with unique app_id. The unique app_id is required to authenticate client with Firework platform. Check more details about its usage in the later sections. 

# How to use library? 

In build.gradle of your app, add 

	dependencies {
		------ other dependencies 
		------ 
		------
		implementation 'com.github.loopsocial:firework_sdk:v1.1.1'
	}

	In build.gradle of your project, add 
	
	allprojects {
    		repositories {
        		--
			---
			--- 
        		maven { url 'https://jitpack.io' }
    		}
	}

# How to integrade firework video feed in your application using FireworkSDK?
FireworkSDK provides two options to integrate Firework video feed in your application.
- Fragment: This out of box solution. You can add FireworkVideoFeed fragment into your view hierarchy and you are good to go.   
- VideoView: This is custom view and wraps FireworkVideoPlayer. It will play the video that you set with setVideo method. 

We will discuss both of these approaches in the subsequent sections. 

**Fragment:**

This is the quickest and easiest way to receiving Firework video feed in your applicaiton. The fragment will handle initializing ```FireworkSDK```, authenticating your applicaiton with ```Firework platform``` and displaying the video feed as well as handling video playback on your behalf in your application. The approach hides all the intricacies of authenticating,  requesting video feed and playing it. 


You can add the following fragment to your view hierarchy with requried parameters and its done.  
				
			<fragment android:id="@+id/{your_fragment_id}"
	   			android:name="com.loopnow.fireworklibrary.views.VideoFeedFragment"
	   			android:layout_width="{your_fragment_width}"
	   			android:layout_height="{your_fragment_height}"
	   			app:showTitle="{true / false}"
	   			app:appid="{provide_your_app_id}"
	   			app:feedLayout="{grid | horizontal | vertical}"
	  			app:columns="{number_of_columns_if_your_feedLayout_is_grid}"
	   			app:textStyle="@style/{your_text_style_for_video_title}" 
           			app:imageStyle="@style/{your_image_style_for_video_thumbnail}">
			</fragment>

1. appId: This refers to app_id you received at the time of registering your application with Firework platform. FireworkSDK will throw exception in the absense of appid. It is a must attribute. Please contact Firework, if you don't already have one. 

2. feedLayout: This is an optional attribute and specifies the layout used to display the video feed. The value of feedLayout can any of the following availale layouts. 

* horizontal: Will layout video feed as a single row and will function as a horizontal scrollable view. 

	<img src="screenshots/Horizontal_video_list.png"  width="270" height="480">

* vertical: Will layout video feed as a single column and will function as a vertical scrollable view.

	<img src="screenshots/vertical.jpg"  width="270" height="480"> <img src="screenshots/vertical_with_title.jpg"  width="270" height="480">


* grid: Will layout video feed in a multiple <rows> x <columns> format. It will scroll vertically. If optional attribute  ```app:columns``` is not specified, the default columm number defaults to 2. Specify app:columns="number of columns you want" to have more or less than 2 columns.  

	<img src="screenshots/grid.jpg"  width="270" height="480"> <img src="screenshots/grid_with_title.jpg"  width="270" height="480">


3. columns: This is an optional attribute and is only relevant if feedLayout is grid. It has default value of 2. 

4. showTitle: This is an optional attribute. It can be either true or false. When true, video title is displayed below the thumbnail. The default value is false and no title is displayed. The number of lines of title and the look and feel such as font, text color, and text size can be customized with optional attribute ```app:textStyle```.

5. textStyle: This is an optional attribute and when specified the style is applied to TextView displaying video title. This attribute has no effect if you have showTitle set to false. 

```app:textStyle="@style/VideoTitleStyle"```
	<style name="VideoTitleStyle">
        	<item name="android:textColor">#ff4a4a4a</item>
        	<item name="android:textSize">14dp</item>
        	<item name="android:lines">2</item>
        	<item name="android:gravity">right</item>
        	<item name="android:layout_width">match_parent</item>
        	<item name="android:fontFamily">@font/squeakychalk</item>
   	</style>
  
Note that the value of attribute lines is also applied to maxLines. 

6. imageStyle: An optional attribute that can be used to define corner radius of the image. At present, only radius is supported. 

```app:imageStyle="@style/ThumbnailStyle"```

	<style name="ImageStyle" >
	       <item name="android:radius">12dp</item>
	</style>
	
	
Pleas refer to source code, to learn more about using VideoFeedFragment 

**VideoView**

FireworkSDK provides view VideoView that you can embed in your view hierarchy. You can have control over player UX/UI.
VideoView provides api to pause - pause(), resume - resume(), seek -seek(milliseconds), get progress - getProgress(), set video to be played - setVideo(videoId)

If you want to use VideoView, you have to first initialize FireworkSDK and request video feed. 

	val fireworkSDK = FireworkSDK.initialize(applicationContext, appid, bundle_id, generateViewId(),
                        object : FireworkInitStatusListener {
                            override fun onInitializing() {
                                // Initializing the Firework SDK.
                            }

                            override fun onInitCompleted() {
                                // Firework SDK initialization completed.
				// Request feed here                               
                            }

                            override fun onInitFailed(error: String) {
                                // Firework SDK initialization failed.                       
                            }
                        })
			
			
As discussed earlier, you receive appId at the time of registering your application with Firework platform, bundle_id is the same as your package name specified in your AndroidManifest and provided at the time of registering your application. You also need to provide unique id, which here is created with generateViewId() but you can choose another way of creating it. The last argument is FireworkInitStatusListener. You will implement three callbacks
1. onInitializing()
2. onInitCompleted()
3. onInitFailed()

Once you initialize SDK, you can request video feed. The example below demonstrates requesting feed and setting and updating data in the adapter.  

	fireworkSDK.getVideoFeed().observe(this, Observer {
            it?.let { result ->
                when (result) {
                    is FeedResult.Loading -> {
                        // Data is being requested from server 
                    }
                    is FeedResult.Error -> {
                        // Error encountered while requesting data 
                    }
                    is FeedResult.Videos -> {
                        // Received data from server 
			// we recommend calling the following method to apply the new recommendated video list order 
                        //  fireworkSDK.preparePlayableList(adapter.videoList, result.videos, adapter.currentVideo, view_pager.offscreenPageLimit)
			
			if (adapter.videoList.size > 0) {
                            fireworkSDK.preparePlayableList(adapter.videoList, result.videos, adapter.currentVideo, view_pager.offscreenPageLimit)
                        } else {
                            if (result.videos.isNotEmpty() && adapter.currentVideo == -1) {
                                
				adapter.setData(result.videos)
                                view_pager.setCurrentItem(0, false)
                                fireworkSDK.nowPlayingVideo(0, adapter.videoList[0].encoded_id)                    
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
	
	
Note that getVideoFeed returns object FeedResult 

	sealed class FeedResult {
    		object Loading : FeedResult()
    		object Error : FeedResult()
    		data class Videos(val videos: List<VideoMetaData>) : FeedResult()
	}
	
	data class VideoMetaData (
        	var encoded_id: String = "",
        	var caption: String? = null,
        	var creator: Creator? = null,
        	var web_share_url: String? = null,
        	var likes_count: Int = 0,
        	var views_count: Int = 0,
        	var thumbnail_url: String?
	)


Also if you are using VideoView, you can add VideoPlaybackStatusListener to get callbacks when video starts buffering, playing and paused. 
	
	fireworkVideoView.addVideoPlaybackStatusListener(new VideoView.VideoPlaybackStatusListener() {
            @Override
            public void buffering() {
                Log.v("PlaybackLog", "Player buffering");
            }

            @Override
            public void playing() {
                Log.v("PlaybackLog", "Player Playing");

            }

            @Override
            public void paused() {
                Log.v("PlaybackLog", "Player Paused");

            }
        });

If you are using ViewPager and PagerAdapter, you can inflate your view hierarchy that contains videoview and set video to be played using setVideo method of VideoView. Please refer to example below. This will looks different, depending on your view hierarchy. 

	override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        	val video = videoList[position]
        	val inflater = LayoutInflater.from(context)
        	val layout = inflater.inflate(R.layout.playbackview_item, collection, false) as ViewGroup
        	val videoView = if(layout.childCount > 0) layout.getChildAt(0) as VideoView else null

        	videoView?.apply {
            		setVideo(video)
        	} 
        	collection.addView(layout)
        	return layout
    	}

For FireworkSDK to personalize the video recommendation, it is important to call method nowPlayingVideo of FireworkSDK. If you were using ViewPager you could do something like this - 

	view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                fireworkSDK.nowPlayingVideo(p0, adapter.videoList[p0])
            }
        })



# Pagination
When VideoFeedFragment is used, it handles pagination and continuously fetches feed as user scrolls. But if you choose to get raw feed and render it youself, you will have to implement pagination. 


# Video Playback
When user clicks on one of the thumbnails from the video feed integrated in your application, FireworkSDK handles the onClick event and starts the video playback. You will have to include PlaybackActivity in your AndroidManifest file. 
```<activity android:name="com.loopnow.fireworklibrary.PlaybackActivity" />```
                

# Video Playback Fragment 
In case you want to integrate full screen video playback without the video feed (thumbnails), you can add FireworkPlayerFragment to your view hierarchy. Users can swipe right to watch the next video and swipe left to watch previous video if any available. 

	<fragment android:name="com.loopnow.fireworklibrary.views.FireworkPlayerFragment"
		  android:layout_width="match_parent"
		  android:layout_height="match_parent"
		  app:appid="{your_app_id}"
		  android:id="{your_fragment_id}">
	</fragment>


