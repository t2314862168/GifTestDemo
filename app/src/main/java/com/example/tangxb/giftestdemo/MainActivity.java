package com.example.tangxb.giftestdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.stream.StreamResourceLoader;
import com.bumptech.glide.load.model.stream.StreamStringLoader;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.tangxb.giftestdemo.glide2gif.GifDrawableByteTranscoder;
import com.example.tangxb.giftestdemo.glide2gif.StreamByteArrayResourceDecoder;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {
    PhotoView mPhotoView01;
    PhotoView mPhotoView02;
    PhotoView mPhotoView03;
    PhotoView mPhotoView04;
    /**
     * 加载网络gif或者是assets文件夹里面的gif或者是SD卡里面的gif上面可用
     */
    GenericRequestBuilder<String, InputStream, byte[], GifDrawable> glideNet;
    /**
     * 加载drawable文件夹里面gif可用
     */
    GenericRequestBuilder<Integer, InputStream, byte[], GifDrawable> glideLocalForDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoView01 = (PhotoView) findViewById(R.id.photoview01);
        mPhotoView02 = (PhotoView) findViewById(R.id.photoview02);
        mPhotoView03 = (PhotoView) findViewById(R.id.photoview03);
        mPhotoView04 = (PhotoView) findViewById(R.id.photoview04);
        mPhotoView01.enable();
        mPhotoView02.enable();
        mPhotoView03.enable();
        mPhotoView04.enable();

        try {
            buildGlideLoadGif(this);
            // 加载assets文件夹里面的gif(或者是SD卡上面的gif)
            String uriPath = "file:///android_asset/gif-02.gif";
            // 这里直接使用的clone,因为用String可以加载
            glideNet.clone().diskCacheStrategy(DiskCacheStrategy.NONE).load(uriPath).into(mPhotoView01);
            GifDrawable gifDrawable01 = new GifDrawable(getAssets(), "gif-02.gif");
            mPhotoView01.setImageDrawable(gifDrawable01);

            // 加载drawable文件夹里面的gif
            int resId = R.drawable.anim_flag_iceland;
            glideLocalForDrawable.load(resId).into(mPhotoView02);

            // 加载网络gif(文件后缀名可以不为gif,也能加载成功)
            String url = "http://img0.imgtn.bdimg.com/it/u=3063115354,3100339602&fm=21&gp=0.jpg";
            glideNet.load(url).into(mPhotoView03);

            // 直接加载assets文件夹里面的gif
            GifDrawable gifDrawable04 = new GifDrawable(getAssets(), "gif-04.gif");
            mPhotoView04.setImageDrawable(gifDrawable04);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        testLargeImg();
    }


    /**
     * <a href="https://github.com/bumptech/glide/issues/805">how to combine android-gif-drawable with glide的参考链接</a>
     *
     * @param context
     */
    private void buildGlideLoadGif(Context context) {
        // make this a field and initialize it once (this is good in list adapters or when used a lot)
        glideNet = Glide
                .with(context)
                .using(new StreamStringLoader(context), InputStream.class)
                .from(String.class) // change this if you have a different model like a File and use StreamFileLoader above
                .as(byte[].class)
                .transcode(new GifDrawableByteTranscoder(), GifDrawable.class) // pass it on
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) // cache original
                .decoder(new StreamByteArrayResourceDecoder())  // load original
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new StreamByteArrayResourceDecoder()));

        glideLocalForDrawable = Glide
                .with(context)
                .using(new StreamResourceLoader(context), InputStream.class)
                .from(Integer.class) // change this if you have a different model like a File and use StreamFileLoader above
                .as(byte[].class)
                .transcode(new GifDrawableByteTranscoder(), GifDrawable.class) // pass it on
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) // cache original
                .decoder(new StreamByteArrayResourceDecoder())  // load original
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<byte[]>(new StreamByteArrayResourceDecoder()));
    }

    /**
     * 加载高清大图
     */
    private void testLargeImg() {
        SubsamplingScaleImageView subsamplingScaleImageView = new SubsamplingScaleImageView(this);
        View decorView = getWindow().getDecorView();
        ViewGroup contentView = (ViewGroup) decorView.findViewById(android.R.id.content);
        contentView.addView(subsamplingScaleImageView);
        subsamplingScaleImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        subsamplingScaleImageView.setMaximumDpi(180);
        // 这里使用了<code>ImageViewState</code>,使得从最开始的位置开始缩放
        subsamplingScaleImageView.setImage(ImageSource.resource(R.drawable.long_img), new ImageViewState(2.0f, new PointF(0, 0), 0));
        subsamplingScaleImageView.setDebug(true);
    }
}
