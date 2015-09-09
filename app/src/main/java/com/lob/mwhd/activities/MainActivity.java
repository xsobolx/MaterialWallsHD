package com.lob.mwhd.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lob.mwhd.R;
import com.lob.mwhd.fragments.AboutFragment;
import com.lob.mwhd.fragments.FlatFragment;
import com.lob.mwhd.fragments.GoogleNowFragment;
import com.lob.mwhd.fragments.MaterialFragment;
import com.lob.mwhd.fragments.MinimalFragment;
import com.lob.mwhd.fragments.PhotographyFragment;
import com.lob.mwhd.fragments.PolyFragment;
import com.lob.mwhd.fragments.UploadWallpaperFragment;
import com.lob.mwhd.fragments.UsersFragment;
import com.lob.mwhd.helpers.Utils;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends ActionBarActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Toolbar toolbar;
    private Drawer drawer;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.lob.mwhd.helpers.FragmentManager.fragmentManager = getSupportFragmentManager();

        activity = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
        setSupportActionBar(toolbar);

        buildDrawer(getSupportFragmentManager());

        sharedPreferences = Utils.getSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        if (Utils.isOnline(getApplicationContext())) {
            Utils.Debug.log(getString(R.string.device_is_connected));
            Utils.setFragment(getSupportFragmentManager(), new MaterialFragment());
            Utils.setStatusBarColor(this);
        } else {
            Utils.showAlertDialogNotOnline(this);
        }
    }

    private void buildDrawer(final FragmentManager fragmentManager) {

        Utils.Debug.log(getString(R.string.building_drawer));
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.material)),
                        new PrimaryDrawerItem().withName(getString(R.string.minimal)),
                        new PrimaryDrawerItem().withName(getString(R.string.flat)),
                        new PrimaryDrawerItem().withName(getString(R.string.google_now)),
                        new PrimaryDrawerItem().withName(getString(R.string.poly)),
                        new PrimaryDrawerItem().withName(getString(R.string.photography)),
                        new PrimaryDrawerItem().withName(getString(R.string.users)),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(getString(R.string.upload_wallpaper)),
                        new SecondaryDrawerItem().withName(getString(R.string.about)),
                        new SecondaryDrawerItem().withName(getString(R.string.ads_info))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (position) {
                            case 0:
                                drawer.closeDrawer();
                                Utils.setFragment(fragmentManager, new MaterialFragment());
                                break;
                            case 1:
                                drawer.closeDrawer();
                                Utils.setFragment(fragmentManager, new MinimalFragment());
                                break;
                            case 2:
                                drawer.closeDrawer();
                                Utils.setFragment(fragmentManager, new FlatFragment());
                                break;
                            case 3:
                                drawer.closeDrawer();
                                Utils.setFragment(fragmentManager, new GoogleNowFragment());
                                break;
                            case 4:
                                drawer.closeDrawer();
                                Utils.setFragment(fragmentManager, new PolyFragment());
                                break;
                            case 5:
                                drawer.closeDrawer();
                                Utils.setFragment(fragmentManager, new PhotographyFragment());
                                break;
                            case 6:
                                drawer.closeDrawer();
                                Utils.setFragment(fragmentManager, new UsersFragment());
                                break;
                            case 7:
                                // It's the divider drawer item
                            case 8:
                                drawer.closeDrawer();
                                Utils.setFragment(fragmentManager, new UploadWallpaperFragment());
                                break;
                            case 9:
                                drawer.closeDrawer();
                                Utils.setFragment(fragmentManager, new AboutFragment());
                                break;
                            case 10:
                                drawer.closeDrawer();
                                showAdDialog();
                                break;
                        }
                        return true;
                    }
                }).build();
    }

    private void showAdDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);

        if (sharedPreferences.getBoolean("mustShowAds", true)) {
            alert.setTitle(getString(R.string.warning));
            alert.setMessage(getString(R.string.ads_message));
            alert.setCancelable(false);
            alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editor.putBoolean("mustShowAds", false).apply();
                    Utils.restart(activity, getApplicationContext());
                }
            });
            alert.setNegativeButton(getString(R.string.support), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), R.string.thank_you, Toast.LENGTH_SHORT).show();
                    editor.putBoolean("mustShowAds", true).apply();
                    Utils.restart(activity, getApplicationContext());
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        } else {
            alert.setTitle(getString(R.string.warning));
            alert.setMessage(getString(R.string.enable_ads_question));
            alert.setCancelable(false);
            alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editor.putBoolean("mustShowAds", true).apply();
                    Toast.makeText(getApplicationContext(), getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
                    Utils.restart(activity, getApplicationContext());
                }
            });
            alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editor.putBoolean("mustShowAds", false).apply();
                    Utils.restart(activity, getApplicationContext());
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_gridview_1_col) {
            editor.putInt("col", 1).apply();
            Utils.restart(activity, getApplicationContext());
        }
        if (id == R.id.action_gridview_2_col) {
            editor.putInt("col", 2).apply();
            Utils.restart(activity, getApplicationContext());
        }
        if (id == R.id.action_gridview_3_col) {
            editor.putInt("col", 3).apply();
            Utils.restart(activity, getApplicationContext());
        }
        return super.onOptionsItemSelected(item);
    }
}
