# Pull-to-Refresh

#### This project aims to provide a simple and customizable pull to refresh implementation. Made in [Eggsy] (https://github.com/eggsywelsh)

#Usage

*For a working implementation, Have a look at the Sample Project - sample*

2. Include the PullToRefreshView widget in your layout.

	```xml
    <com.eggsy.ui.PullToRefreshView
            android:id="@+id/pull_to_refresh"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            refreshView:topDragDistance="120dp"
            refreshView:maxTopDragDistance="160dp"
            refreshView:bottomDragDistance="30dp"
            refreshView:maxBottomDragDistance="60dp"
            refreshView:enableBottomToRefresh="true"
            refreshView:enableTopToRefresh="true"
            refreshView:enableTopElastic="false"
            refreshView:enableBottomElastic="false"
            android:background="@android:color/white"
            >

            <android.support.v7.widget.RecyclerView
                android:id="@+id/recycler_view"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:divider="@null"
                android:dividerHeight="0dp"
                android:fadingEdge="none" />

        </com.eggsy.ui.PullToRefreshView>
    ```

3. In your `onCreate` method refer to the View and setup OnRefreshListener.
	```java
    mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);

            mPullToRefreshView.setTopRefreshView(new LightRefreshView(getActivity(), mPullToRefreshView));

            MoreRefreshView moreRefreshView = new MoreRefreshView(getActivity(),
                                (AnimationDrawable) getResources().getDrawable(R.drawable.list_bottom_load_more));
            mPullToRefreshView.setBottomRefreshView(moreRefreshView);

            mPullToRefreshView.setOnTopRefreshListener(new PullToRefreshView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPullToRefreshView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshView.setTopRefreshing(false);
                        }
                    }, REFRESH_DELAY);
                }
            });

            mPullToRefreshView.setOnBottomRefreshListener(new PullToRefreshView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPullToRefreshView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshView.setBottomRefreshing(false);
                        }
                    }, REFRESH_DELAY);
                }
            });
     ```

#Customization

To customize drawables you can change:
   * sun.png - Sun image
   * sky.png - background image
   * buildings.png - foreground image

# Misc
If you need to change progress state:
```java
	mPullToRefreshView.setRefreshing(boolean isRefreshing)
```
#Compatibility
  
  * Android GINGERBREAD 2.3+

#### Let us know!

We’d be really happy if you sent us links to your projects where you use our component. Just send an email to eggsywelsh@gmail.com And do let us know if you have any questions or suggestion regarding the animation.

## License

    Copyright 2017, Eggsy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
