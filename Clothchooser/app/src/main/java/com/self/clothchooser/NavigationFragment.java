package com.self.clothchooser;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment implements View.OnClickListener {
    private LinearLayout layout_addnew, layout_randompair, layout_bookmark;
    private String version;
    private TextView textView_version, textView_addnew, textView_random, textView_bookmark;

    public NavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        layout_addnew = (LinearLayout) view.findViewById(R.id.layout_addpair);
        textView_addnew = (TextView) view.findViewById(R.id.tv_addnew);
        textView_random = (TextView) view.findViewById(R.id.tv_random);
        textView_bookmark = (TextView) view.findViewById(R.id.tv_bookmark);
        layout_bookmark = (LinearLayout) view.findViewById(R.id.layout_bookmark);
        layout_randompair = (LinearLayout) view.findViewById(R.id.layout_pairrandom);
        textView_version = (TextView) view.findViewById(R.id.tv_version);
        layout_randompair.setOnClickListener(this);
        layout_bookmark.setOnClickListener(this);
        layout_addnew.setOnClickListener(this);
        try {
            appversion();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        textView_version.setText("App Version: " + version);
        animation();
        return view;
    }

    private void animation() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide);
        Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide2);
        Animation animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide3);
        textView_addnew.setAnimation(animation);
        textView_random.setAnimation(animation2);
        textView_bookmark.setAnimation(animation3);
    }

    @Override
    public void onClick(View v) {
        if (v == layout_addnew) {
            startActivity(new Intent(getActivity(), Add_clothActivity.class));
            getActivity().finish();
        } else if (v == layout_bookmark) {
            startActivity(new Intent(getActivity(), BookmarkActivity.class));
            getActivity().finish();
        } else if (v == layout_randompair) {
            startActivity(new Intent(getActivity(), WearTodayActivity.class));
            getActivity().finish();
        }
    }

    private void appversion() throws PackageManager.NameNotFoundException {
        PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        version = info.versionName;
    }
}
