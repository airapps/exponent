// Copyright 2015-present 650 Industries. All rights reserved.

package abi5_0_0.host.exp.exponent.modules;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import abi5_0_0.com.facebook.react.bridge.Promise;
import abi5_0_0.com.facebook.react.bridge.ReactApplicationContext;
import abi5_0_0.com.facebook.react.bridge.ReactContextBaseJavaModule;
import abi5_0_0.com.facebook.react.bridge.ReactMethod;
import abi5_0_0.com.facebook.react.common.MapBuilder;
import host.exp.exponent.ExponentApplication;
import host.exp.exponent.kernel.Kernel;

public class ExURLHandlerModule extends ReactContextBaseJavaModule {

  @Inject
  Kernel mKernel;

  public ExURLHandlerModule(ReactApplicationContext reactContext, ExponentApplication application) {
    super(reactContext);
    application.getAppComponent().inject(this);
  }

  @Override
  public String getName() {
    return "EXURLHandler";
  }

  public Map<String, Object> getConstants() {
    List<String> schemes = new ArrayList<>();
    schemes.add("exp");
    schemes.add("exps");

    Map<String, Object> constants = MapBuilder.of(
        "schemes", schemes,
        "initialURL", null,
        "settingsURL", "http://settings"
    );
    // source application, referrer
    return constants;
  }

  @ReactMethod
  public void openURLAsync(String url, Promise promise) {
    if (url == null) {
      promise.resolve(false);
    }

    Uri uri = Uri.parse(url);
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    PackageManager packageManager = getReactApplicationContext().getPackageManager();
    if (intent.resolveActivity(packageManager) != null) {
      mKernel.getActivityContext().startActivity(intent);
      promise.resolve(true);
    } else {
      promise.resolve(false);
    }
  }

  @ReactMethod
  public void canOpenURLAsync(String url, Promise promise) {
    if (url == null) {
      promise.resolve(false);
    }

    Uri uri = Uri.parse(url);
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    PackageManager packageManager = getReactApplicationContext().getPackageManager();
    promise.resolve(intent.resolveActivity(packageManager) != null);
  }
}
