package com.example.taxibill.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.taxibill.R;
import com.example.taxibill.databinding.BottomsheetCameraGalleryBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraHelper implements CommonConstants {

  /*  public static boolean checkPermissions(Fragment fragment) {
        return ContextCompat.checkSelfPermission(fragment.getContext(), CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(fragment.getContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }*/



    public static File createImageFile1(Context context) throws IOException {



        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                timeStamp,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return image;

    }

    public static void launchPictureIntentExpense(final Activity activity, final Fragment fragment, View view, final File file, final int code1, final int code2) {


        BottomSheetDialog filesBottom;
        BottomsheetCameraGalleryBinding cameraGalleryBinding;
        cameraGalleryBinding = BottomsheetCameraGalleryBinding.inflate(activity.getLayoutInflater(), null, false);
        filesBottom = DialogUtils.getBottomDialog(activity, cameraGalleryBinding.getRoot());

        cameraGalleryBinding.openfileTitle.setText("Attach Bill");

        cameraGalleryBinding.CameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filesBottom.dismiss();
                dispatchTakePictureIntentFragment(activity, fragment, file, code1);
            }
        });

        cameraGalleryBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filesBottom.dismiss();
            }
        });


        cameraGalleryBinding.GalleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                filesBottom.dismiss();

              /*  Intent takePictureIntent;
                takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                takePictureIntent.setType("image/*");

                launcher.launch(takePictureIntent);*/


                Intent takePictureIntent;
                takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                takePictureIntent.setType("image/*");

                if (fragment != null)

                    fragment.startActivityForResult(takePictureIntent, code2);
                else activity.startActivityForResult(takePictureIntent, code2);
            }
        });

        filesBottom.show();






    }



    public static void dispatchTakePictureIntentFragmentMultipleImages(Activity activity, ActivityResultLauncher<Intent> activityResultLauncher, File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go

            // Continue only if the File was successfully created
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        activity.getPackageName() + ".provider",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activityResultLauncher.launch(takePictureIntent);
            } else {
                Toast.makeText(activity, "File is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Intent is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public static void launchPictureRentIntent(final Activity activity, ActivityResultLauncher<Intent> activityResultLauncher, ActivityResultLauncher<Intent> launcher, View view, final File file) {

        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_image_menu_item, popup.getMenu());

        //inflater.inflate(R.menu.menu_pdf, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_gallery_item) {
                    Intent takePictureIntent;
                    takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    takePictureIntent.setType("image/*");

                    launcher.launch(takePictureIntent);

                  /*  Intent takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    takePictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    takePictureIntent.setType("image/*");
                    launcher.launch(takePictureIntent);*/

                }

                if (id == R.id.menu_camera_item) {
                    RentdispatchTakePictureIntentFragment(activity, activityResultLauncher, file);
                }

                return false;
            }
        });
        popup.show();

    }


//    launchPictureRentIntent1

    public static void launchPictureRentIntent1(final Activity activity, ActivityResultLauncher<Intent> activityResultLauncher, ActivityResultLauncher<Intent> launcher, final File file) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        BottomsheetCameraGalleryBinding binding = BottomsheetCameraGalleryBinding.inflate(LayoutInflater.from(activity));
        bottomSheetDialog.setContentView(binding.getRoot());

        bottomSheetDialog.show();

        binding.CameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RentdispatchTakePictureIntentFragment(activity, activityResultLauncher, file);
                bottomSheetDialog.dismiss();
            }
        });

        binding.GalleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent;
                takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                takePictureIntent.setType("image/*");

                launcher.launch(takePictureIntent);
                bottomSheetDialog.dismiss();
            }
        });

    }



  /*  public static void launchPictureRentIntent(final Activity activity, ActivityResultLauncher<Intent> galleryLauncher, ActivityResultLauncher<Intent> cameraLauncher, View view, final File file) {
        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_image_menu_item, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_gallery_item) {
                    Intent takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    takePictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    takePictureIntent.setType("image/*");
                    galleryLauncher.launch(takePictureIntent);
                } else if (id == R.id.menu_camera_item) {
                    RentdispatchTakePictureIntentFragment(activity, cameraLauncher, file);
                }
                return false;
            }
        });
        popup.show();
    }
*/


    public static void RentdispatchTakePictureIntentFragment(Activity activity, ActivityResultLauncher<Intent> activityResultLauncher, File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent


        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go

            // Continue only if the File was successfully created
            Uri photoURI = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".provider",
                    file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            activityResultLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(activity, "Intent is empty", Toast.LENGTH_SHORT).show();
        }
    }


    public static void dispatchTakePictureIntentFragmentRental(Activity activity, ActivityResultLauncher<Intent> activityResultLauncher, File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent


        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go

            // Continue only if the File was successfully created
            Uri photoURI = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".provider",
                    file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            activityResultLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(activity, "Intent is empty", Toast.LENGTH_SHORT).show();
        }
    }


    public static void dispatchTakePictureIntentFragment(Activity activity, Fragment fragment, File file, int code) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go

            // Continue only if the File was successfully created
            if (file != null) {
                Log.i("Image", activity.getPackageName());
                Uri photoURI = FileProvider.getUriForFile(activity,
                        activity.getPackageName() + ".provider",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (fragment != null)
                    fragment.startActivityForResult(takePictureIntent, code);
                else
                    activity.startActivityForResult(takePictureIntent, code);
            } else {
                Toast.makeText(activity, "File is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Intent is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public static void dispatchTakePictureIntentFragmenNew(Activity activity, ActivityResultLauncher<Intent> launcher, File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go

            // Continue only if the File was successfully created
            if (file != null) {
                Log.i("Image", activity.getPackageName());
                Uri photoURI = FileProvider.getUriForFile(activity,
                        activity.getPackageName() + ".provider",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                launcher.launch(takePictureIntent);


            } else {
                Toast.makeText(activity, "File is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Intent is empty", Toast.LENGTH_SHORT).show();
        }
    }



    public static void launchPictureIntentRental(final Activity activity, ActivityResultLauncher<Intent> activityResultLauncher, ActivityResultLauncher<Intent> launcher, View view, final File file) {

        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_image_menu_item, popup.getMenu());

        //inflater.inflate(R.menu.menu_pdf, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_gallery_item) {
                    Intent takePictureIntent;
                    takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    takePictureIntent.setType("image/*");

                    launcher.launch(takePictureIntent);

                }
               /* if (id == R.id.menu_pdf) {
                    Intent takePictureIntent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    } else {
                        takePictureIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    }
                    takePictureIntent.setType("application/pdf");

                    fragment.startActivityForResult(takePictureIntent, code3);
                }*/
                if (id == R.id.menu_camera_item) {
                    dispatchTakePictureIntentFragmenNew(activity, activityResultLauncher, file);
                }
               /* bundle.putString("currentTitle",((HomeActivity)getActivity()).getSupportActionBar().getTitle().toString());
                ChooseImageFragment fragment = new ChooseImageFragment();
                fragment.setArguments(bundle);
                fragment.setFragmentResultListener(MessagesFragment.this);
                FragmentHelper.addFragment(getActivity(), FRAME_HOME, fragment);*/
                return false;
            }
        });
        popup.show();

    }


    public static File createImageFile(Context context) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                timeStamp,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return image;

    }

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }


    public static int getRotationCompensation(String cameraId, Activity activity, boolean isFrontFacing) {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation = ORIENTATIONS.get(deviceRotation);

        // Get the device's sensor orientation.
        try {
            CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            int sensorOrientation = manager
                    .getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.SENSOR_ORIENTATION);

            if (isFrontFacing) {
                rotationCompensation = (sensorOrientation + rotationCompensation) % 360;
            } else { // back-facing
                rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return rotationCompensation;
    }

    public static String getIdFrontalCamera(Context activity) {
        try {
            CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            for (String id : manager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(id);
                //Seek frontal camera.
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    Log.i("CameraID", "Camara frontal id " + id);
                    return id;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String getIdRearCamera(Context activity) {
        try {
            CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            for (String id : manager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(id);
                //Seek frontal camera.
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    Log.i("CameraID", "Camara frontal id " + id);
                    return id;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static byte[] getBytesFromBitmap(String file) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap bitmap = ResizeImage.rotateBitmap(BitmapFactory.decodeFile(file, bmOptions), orientation);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public static void launchPictureIntentRent(final Activity activity, ActivityResultLauncher<Intent> activityResultLauncher, ActivityResultLauncher<Intent> launcher, View view, final File file) {

        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_image_menu_item, popup.getMenu());

        //inflater.inflate(R.menu.menu_pdf, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_gallery_item) {
                    Intent takePictureIntent;
                    takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    takePictureIntent.setType("image/*");

                    launcher.launch(takePictureIntent);

                }
               /* if (id == R.id.menu_pdf) {
                    Intent takePictureIntent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    } else {
                        takePictureIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    }
                    takePictureIntent.setType("application/pdf");

                    fragment.startActivityForResult(takePictureIntent, code3);
                }*/
                if (id == R.id.menu_camera_item) {
                    dispatchTakePictureIntentFragmentRental(activity, activityResultLauncher, file);
                }
               /* bundle.putString("currentTitle",((HomeActivity)getActivity()).getSupportActionBar().getTitle().toString());
                ChooseImageFragment fragment = new ChooseImageFragment();
                fragment.setArguments(bundle);
                fragment.setFragmentResultListener(MessagesFragment.this);
                FragmentHelper.addFragment(getActivity(), FRAME_HOME, fragment);*/
                return false;
            }
        });
        popup.show();

    }

   /* public static void launchPictureIntentNew(Activity activity, View view, ActivityResultLauncher<Intent> launcherCam, ActivityResultLauncher<Intent> launcherGal, boolean multi, File file) {
        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_camera, popup.getMenu());
        //inflater.inflate(R.menu.menu_pdf, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_gallery) {
                    openFilePicker(launcherGal, new String[]{"image/jpeg", "image/jpg", "image/png"}, multi);
                }
                if (id == R.id.menu_pdf) {
                    Intent takePictureIntent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    } else {
                        takePictureIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    }
                    takePictureIntent.setType("application/pdf");

                    fragment.startActivityForResult(takePictureIntent, code3);
                }
                if (id == R.id.menu_camera) {
                    dispatchTakePictureIntentFragmenNew(activity, launcherCam, file);
                }
                bundle.putString("currentTitle",((HomeActivity)getActivity()).getSupportActionBar().getTitle().toString());
                ChooseImageFragment fragment = new ChooseImageFragment();
                fragment.setArguments(bundle);
                fragment.setFragmentResultListener(MessagesFragment.this);
                FragmentHelper.addFragment(getActivity(), FRAME_HOME, fragment);
                return false;
            }
        });
        popup.show();


    }*/

    public static void openFilePicker(ActivityResultLauncher<Intent> launcher, String[] strings, boolean multi) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, strings);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multi);
        launcher.launch(intent);
    }


    public static void launchPictureIntentMulti(final Activity activity, ActivityResultLauncher<Intent> activityResultLauncher, ActivityResultLauncher<Intent> launcher, View view, final File file) {

        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_image_menu_item, popup.getMenu());

        //inflater.inflate(R.menu.menu_pdf, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_gallery_item) {
                    Intent takePictureIntent;

                    takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    takePictureIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    takePictureIntent.setType("image/*");

                    launcher.launch(takePictureIntent);

                }
               /* if (id == R.id.menu_pdf) {
                    Intent takePictureIntent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    } else {
                        takePictureIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    }
                    takePictureIntent.setType("application/pdf");

                    fragment.startActivityForResult(takePictureIntent, code3);
                }*/
                if (id == R.id.menu_camera_item) {
                    dispatchTakePictureIntentFragmenNew(activity, activityResultLauncher, file);
                }
               /* bundle.putString("currentTitle",((HomeActivity)getActivity()).getSupportActionBar().getTitle().toString());
                ChooseImageFragment fragment = new ChooseImageFragment();
                fragment.setArguments(bundle);
                fragment.setFragmentResultListener(MessagesFragment.this);
                FragmentHelper.addFragment(getActivity(), FRAME_HOME, fragment);*/
                return false;
            }
        });
        popup.show();

    }

    public static void launchPictureIntentMultiDoc(final Activity activity, ActivityResultLauncher<Intent> activityResultLauncher, ActivityResultLauncher<Intent> launcher, View view, final File file) {

        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_image_menu_item, popup.getMenu());

        //inflater.inflate(R.menu.menu_pdf, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_gallery_item) {
                    openFilePicker(launcher, new String[]{"image/jpg", "image/jpeg", "application/pdf"}, true);

                }
               /* if (id == R.id.menu_pdf) {
                    Intent takePictureIntent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    } else {
                        takePictureIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    }
                    takePictureIntent.setType("application/pdf");

                    fragment.startActivityForResult(takePictureIntent, code3);
                }*/
                if (id == R.id.menu_camera_item) {
                    dispatchTakePictureIntentFragmenNew(activity, activityResultLauncher, file);
                }
               /* bundle.putString("currentTitle",((HomeActivity)getActivity()).getSupportActionBar().getTitle().toString());
                ChooseImageFragment fragment = new ChooseImageFragment();
                fragment.setArguments(bundle);
                fragment.setFragmentResultListener(MessagesFragment.this);
                FragmentHelper.addFragment(getActivity(), FRAME_HOME, fragment);*/
                return false;
            }
        });
        popup.show();

    }


}