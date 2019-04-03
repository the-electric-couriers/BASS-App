package com.electriccouriers.bass.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.interfaces.CheckInDialogCloseListener;
import com.electriccouriers.bass.models.User;
import com.electriccouriers.bass.preferences.PreferenceHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.Nullable;

public class CheckInSheetDialogFragment extends BottomSheetDialogFragment implements RequestListener {

    private ProgressBar spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkin_dialog, container, false);

        ImageView kaartFront = view.findViewById(R.id.popup_imageKaart);
        spinner = view.findViewById(R.id.popup_cardProgressBar);

        User mainUser = User.create(PreferenceHelper.read(getContext(), Globals.PrefKeys.MAIN_USER));
        String cardImageURL = Globals.API_BASEURL + "user/card/" + mainUser.getUserID() + "/" + mainUser.getFullName() + "/" + mainUser.getAccessCode();
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);

        GlideUrl glideUrl = new GlideUrl(cardImageURL, new LazyHeaders.Builder()
                .addHeader("Authorization", PreferenceHelper.read(getContext(), Globals.PrefKeys.UTOKEN)).build());

        Glide.with(this).load(glideUrl).apply(options).listener(this).into(kaartFront);

        kaartFront.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        spinner.setVisibility(View.GONE);
        return false;
    }

    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();

        if(activity instanceof CheckInDialogCloseListener)
            ((CheckInDialogCloseListener)activity).onDialogClose(dialog);
    }
}
