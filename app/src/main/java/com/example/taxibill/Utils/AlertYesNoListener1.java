package com.example.taxibill.Utils;

import android.os.Bundle;

public interface AlertYesNoListener1 {

    void onYesClick(int which, String requestCode, Bundle bundle);
    void onNoClick(String requestCode);
    void onNeutralClick(String requestCode);
}
