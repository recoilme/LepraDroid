package com.home.lepradroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.home.lepradroid.commons.Commons;
import com.home.lepradroid.objects.BaseItem;
import com.home.lepradroid.objects.Post;
import com.home.lepradroid.utils.ImageLoader;
import com.home.lepradroid.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class PostsAdapter extends ArrayAdapter<BaseItem>
{
    private UUID                groupId;
    private List<BaseItem>      posts           = Collections.synchronizedList(new ArrayList<BaseItem>());
    private LayoutInflater      aInflater;
    private ImageLoader         imageLoader;
    private AQuery listAq;
            
    public PostsAdapter(Context context, UUID groupId, int textViewResourceId,
            ArrayList<BaseItem> posts)
    {
        super(context, textViewResourceId, posts);
        this.posts = posts;
        this.groupId = groupId;
        imageLoader = new ImageLoader(LepraDroidApplication.getInstance());
        aInflater = LayoutInflater.from(getContext());
        listAq = new AQuery(context);
    }

    public int getCount() 
    {
        return posts.size();
    }
    
    public BaseItem getItem(int position) 
    {
        return posts.get(position);
    }
    
    public long getItemId(int position) 
    { 
        return position;
    }
    
    public void updateData(List<BaseItem> posts)
    {
        this.posts.clear();
        this.posts.addAll(posts);
    }
    
    public void addProgressElement()
    {
        if(!posts.isEmpty() && posts.get(posts.size() - 1) != null)
            posts.add(null);   
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        Post post = (Post)getItem(position);
        

        if (convertView==null) {
            if(post != null)
                convertView = aInflater.inflate(R.layout.post_row_view, parent, false);
            else
                convertView =  aInflater.inflate(R.layout.footer_view, null);

        }

        final AQuery aq = listAq.recycle(convertView);
        if (null == post) {
            return convertView;
        }

        String tbUrl = ""+post.getImageUrl();
        if(!TextUtils.isEmpty(""+post.getImageUrl()))
        {
            aq.id(R.id.image).visible().image(post.getImageUrl(),true,true,320,0,null,AQuery.FADE_IN_NETWORK, AQuery.ANCHOR_DYNAMIC);
        }
        else
        {
            aq.id(R.id.image).gone();
        }

        String text = post.getText();
        aq.id(R.id.text).text(TextUtils.isEmpty(text) ? " ..." : text);


        //final TextView authorView = aq.id(R.id.author).getTextView();
        aq.id(R.id.author).text(Html.fromHtml(post.getSignature()));

        //final TextView commentsView = aq.id(R.id.comments).getTextView();
        aq.id(R.id.comments).text(Utils.getCommentsStringFromPost(post));

        //final ImageView stars = aq.id(R.id.stars).getImageView();
        aq.id(R.id.stars).visibility(View.GONE);

        if(post.isGolden())
        {
            aq.id(R.id.stars).image(R.drawable.ic_stars);
            aq.id(R.id.stars).visibility(View.VISIBLE);
        }
        else if(post.isSilver())
        {
            aq.id(R.id.stars).image(R.drawable.ic_wasstars);
            aq.id(R.id.stars).visibility(View.VISIBLE);
        }

        //final TextView ratingView = aq.id(R.id.rating).getTextView();
        if(groupId.equals(Commons.INBOX_POSTS_ID) || post.isVoteDisabled())
            aq.id(R.id.rating).visibility(View.GONE);
        else
            aq.id(R.id.rating).text(Utils.getRatingStringFromBaseItem(post, post.getVoteWeight()));

        return convertView;
    }
}
