package com.tanyixiu.mimo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.adapters.BookItemLoader;
import com.tanyixiu.mimo.moduls.BookItem;
import com.tanyixiu.mimo.utils.ToastUtils;

import java.io.IOException;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mimo on 2015/9/11.
 */
public class BookEditActivity extends BaseActivity {

    private View mRootView;
    private ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(R.layout.activity_book_edit, null);
        setContentView(mRootView);
        initView();
    }

    private void initView() {
        mViewHolder = new ViewHolder(mRootView);

        mViewHolder.mBookeditImgBack.setOnClickListener(mCancelClickListener);
        mViewHolder.mBookeditBtnCancel.setOnClickListener(mCancelClickListener);

        mViewHolder.mBookeditBtnSave.setOnClickListener(mSaveClickListenter);
        mViewHolder.mBookeditLlCover.setOnClickListener(mCoverClickListener);
    }

    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener mSaveClickListenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnSaveClick();
        }
    };

    private View.OnClickListener mCoverClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnLoadImageClick();
        }
    };

    private void btnSaveClick() {
        final BookItem item = mViewHolder.saveBook();

        if (TextUtils.isEmpty(item.getName())) {
            ToastUtils.showLong("请输入书名......");
            return;
        }
        if (TextUtils.isEmpty(item.getAuthor())) {
            ToastUtils.showLong("请输入作者......");
            return;
        }
        if (TextUtils.isEmpty(item.getDigest())) {
            ToastUtils.showLong("请输入摘要......");
            return;
        }

        final Bitmap bitmap = mViewHolder.saveBitmap();
        new BookItemLoader().saveBookItem(item, bitmap, new BookItemLoader.OnBookItemSaveListener() {
            @Override
            public void onSaved(boolean success) {
                Intent intent = new Intent();
                intent.putExtra("bookitem", item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void btnLoadImageClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 != requestCode) {
            return;
        }
        if (null == data) {
            return;
        }
        Bitmap bitmap = null;
        Uri uri = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null == bitmap) {
            return;
        }

        mViewHolder.mBookeditEdLoadHint.setVisibility(View.GONE);
        mViewHolder.mBookeditImgCover.setVisibility(View.VISIBLE);
        mViewHolder.mBookeditImgCover.setImageBitmap(bitmap);
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'activity_book_edit.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @InjectView(R.id.bookedit_img_back)
        ImageView mBookeditImgBack;
        @InjectView(R.id.bookedit_ed_name)
        EditText mBookeditEdName;
        @InjectView(R.id.bookedit_ed_author)
        EditText mBookeditEdAuthor;
        @InjectView(R.id.bookedit_ed_load_hint)
        EditText mBookeditEdLoadHint;
        @InjectView(R.id.bookedit_img_cover)
        ImageView mBookeditImgCover;
        @InjectView(R.id.bookedit_ll_cover)
        LinearLayout mBookeditLlCover;
        @InjectView(R.id.bookedit_ed_digest)
        EditText mBookeditEdDigest;
        @InjectView(R.id.bookedit_btn_cancel)
        Button mBookeditBtnCancel;
        @InjectView(R.id.bookedit_btn_save)
        Button mBookeditBtnSave;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public BookItem saveBook() {
            BookItem item = new BookItem();
            item.setId(UUID.randomUUID().toString());
            item.setState(BookItem.BooKState.NOT_READ);
            item.setName(String.valueOf(mBookeditEdName.getText()));
            item.setAuthor(String.valueOf(mBookeditEdAuthor.getText()));
            item.setDigest(String.valueOf(mBookeditEdDigest.getText()));
            return item;
        }

        public Bitmap saveBitmap() {
            if (mBookeditImgCover.getVisibility() != View.VISIBLE) {
                return null;
            }
            Drawable drawable = mBookeditImgCover.getDrawable();
            if (null == drawable && !(drawable instanceof BitmapDrawable)) {
                return null;
            }
            return drawableToBitmap(drawable);
        }

        public static Bitmap drawableToBitmap(Drawable drawable) {

            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            //canvas.setBitmap(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }
}
