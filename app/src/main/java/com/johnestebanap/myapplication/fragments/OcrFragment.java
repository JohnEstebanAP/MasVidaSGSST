package com.johnestebanap.myapplication.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.johnestebanap.myapplication.R;


public class OcrFragment extends Fragment {

    private static final int RESULTS_TO_SHOW = 10;
    TextView textView, textCarnet, textCedula;
    View viewCarnet, viewCedula;
    CardView cardCarnet;
    CardView cardCedula;
    CircularProgressIndicator progressIndicator;
    boolean imageSelect = Boolean.parseBoolean(null);
    ImageView imgProgresIndicator;
    int REQUEST_CODE = 200;
    private ImageView mImageView;
    private ImageView mImageView2;
    private Bitmap mSelectedImage;
    private Integer mImageMaxWidth;
    private Integer mImageMaxHeight;
    private String docCarnet = "";
    private String docCedula = "";
    private String cedulavalue;
    private TextView viewPorcentajeCarnet;
    private TextView viewPorcentajeCedula;
    private TextInputEditText inputValidacionCedula;
    private TextInputLayout lyValidacionCedula;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ocr, container, false);


        mImageView = view.findViewById(R.id.image_view);
        mImageView2 = view.findViewById(R.id.image_view2);
        cardCarnet = view.findViewById(R.id.card_carnet);
        cardCedula = view.findViewById(R.id.card_cedula);
        textCarnet = view.findViewById(R.id.txt_carnet);
        textCedula = view.findViewById(R.id.txt_cedula);
        viewCarnet = view.findViewById(R.id.view_carnet);
        viewCedula = view.findViewById(R.id.view_cedula);
        viewPorcentajeCarnet = view.findViewById(R.id.text_porcentaje_carnet);
        viewPorcentajeCedula = view.findViewById(R.id.text_porcentaje_cedula);
        inputValidacionCedula = view.findViewById(R.id.input_validacion_cedula);
        lyValidacionCedula = view.findViewById(R.id.ly_validacion_cedula);
        progressIndicator = view.findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);
        imgProgresIndicator = view.findViewById(R.id.img_progres_indicator);
        verificarPermisosCamara();

        cardCarnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelect = true;
                if (!inputValidacionCedula.getText().toString().equals("")) {
                    camara();
                } else {
                    if (inputValidacionCedula.length() >= 6) {
                        lyValidacionCedula.setError(null);
                    } else {
                        lyValidacionCedula.setError("ingrese por favor primero la cedula");
                    }
                }
            }
        });

        cardCedula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelect = false;
                if (!inputValidacionCedula.getText().toString().equals("")) {
                    camara();
                } else {
                    if (inputValidacionCedula.length() >= 6) {
                        lyValidacionCedula.setError(null);
                    } else {
                        lyValidacionCedula.setError("ingrese por favor primero la cedula");
                    }
                }
            }
        });

        inputValidacionCedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //System.out.println(s.toString() + " " + start + " " + count + " " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //System.out.println(s.toString() + " " + start + " " + count);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (inputValidacionCedula.length() >= 6) {
                    lyValidacionCedula.setError(null);
                } else {
                    lyValidacionCedula.setError("Ingrese por favor la cedula completa, esta debe tener mas de 6 dígitos");
                }
            }
        });
        return view;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap imgBitmap = (Bitmap) extras.get("data");

                        if (imageSelect == true) {
                            viewCarnet.setVisibility(View.INVISIBLE);
                            textCarnet.setVisibility(View.INVISIBLE);
                            mImageView.setImageBitmap(imgBitmap);
                        } else {
                            viewCedula.setVisibility(View.INVISIBLE);
                            textCedula.setVisibility(View.INVISIBLE);
                            mImageView2.setImageBitmap(imgBitmap);
                        }
                        onItemSelected(imgBitmap);
                        runTextRecognition();
                    }
                }
            });

    private void camara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity( getContext().getPackageManager()) != null) {
           someActivityResultLauncher.launch(intent);
        }
    }

    private void runTextRecognition() {
        InputImage image = InputImage.fromBitmap(mSelectedImage, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {
                processTextRecognitionResult(text);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                //metodo de retoma de foto hasta que sea exitoso
            }
        });
    }

    private void processTextRecognitionResult(Text texts) {
        // List<Text.TextBlock> blocks = texts.getTextBlocks();
        if (imageSelect) {
            docCarnet = texts.getText();
            viewPorcentajeCarnet.setBackgroundResource(R.color.color_green);
        } else {
            docCedula = texts.getText();
            viewPorcentajeCedula.setBackgroundResource(R.color.color_green);
        }

        cargarBarraProgreso();
        if (!docCarnet.equals("") && !docCedula.equals("")) {
            comparacionDatos(docCarnet, docCedula);
        } else {

            if (docCarnet.isEmpty()) {
                viewPorcentajeCarnet.setBackgroundResource(R.color.color_red);
                showToast("Por favor tomar la foto del carnet");
            }

            if (docCedula.isEmpty()) {
                viewPorcentajeCedula.setBackgroundResource(R.color.color_red);
                showToast("Por favor tomar la foto de la cedula");
            }
        }


    }

    public void comparacionDatos(String datosCarnet, String datosCedula) {

        String[] miVacuna = {"M", "i", "V", "a", "c", "u", "n", "a"};
        String[] covid19 = {"C", "o", "v", "i", "d", "-", "1", "9"};

        cedulavalue = inputValidacionCedula.getText().toString();
        datosCarnet = datosCarnet.replace(" ", "\n");
        datosCarnet = datosCarnet.replace(".", "");
        datosCedula = datosCedula.replaceAll("[^0-1-2-3-4-5-6-7-8-9]", "");

        String[] datosCarnetVector = datosCarnet.split("\n");
        String[] datosCedulaVector = datosCedula.split("\n");
        String[] cedulaVector = cedulavalue.split("");


        double[] resultadoMivacuna = validar(datosCarnetVector, miVacuna, "MiVacuna");
        double[] resultadoCovid19 = validar(datosCarnetVector, covid19, "Covid-19");
        double[] resultadoCedulaCarnet = validarCedulaCarnet(datosCarnetVector, cedulaVector, cedulavalue);

        double[] resultadoCedula = validar(datosCedulaVector, cedulaVector, cedulavalue);


        Boolean validacion = true;
        double[] porcentajesCarnet = new double[3];

        if (resultadoMivacuna[0] == 1.0) {
            porcentajesCarnet[0] = resultadoMivacuna[2];
            validacion = false;
        }

        if (resultadoCovid19[0] == 1) {
            porcentajesCarnet[1] = resultadoCovid19[2];
            validacion = false;
        }


        String cedulaCarnetComparacion = "";
        String cedulaComparacion = "";

        if (resultadoCedulaCarnet[0] == 1) {
            cedulaCarnetComparacion =String.valueOf(datosCarnetVector[(int) resultadoCedulaCarnet[1]]);
            porcentajesCarnet[2] = resultadoCedulaCarnet[2];
            validacion = false;
        } else {
            showToast("Cedula del carnet no valida, por favor tomar la foto nuevamente");
        }

        //Metodo para que nos muestre la barra de progreso por 5 segundos y desaparesca alterminar

        cargarBarraProgresoComparacion();
        double totalPorcentajeCarnet = (porcentajesCarnet[0] + porcentajesCarnet[2]) / 2;

        if (resultadoMivacuna[0] == 1 || resultadoCedulaCarnet[0] == 1) {
            viewPorcentajeCarnet.setText(totalPorcentajeCarnet + "%");
            viewPorcentajeCarnet.setBackgroundResource(R.color.color_green);
            validacion = false;
        } else {
            viewPorcentajeCarnet.setText(totalPorcentajeCarnet + "%");
            viewPorcentajeCarnet.setBackgroundResource(R.color.color_red);
            validacion = true;
        }

        if (resultadoCedula[0] == 1) {
            cedulaComparacion =String.valueOf(datosCedulaVector[(int) resultadoCedula[1]]);
            viewPorcentajeCedula.setText(resultadoCedula[2] + "%");
            viewPorcentajeCedula.setBackgroundResource(R.color.color_green);
            validacion = false;
        } else {
            viewPorcentajeCedula.setText(resultadoCedula[2] + "%");
            viewPorcentajeCedula.setBackgroundResource(R.color.color_red);
        }
        double[] validacionCedulasvector = compararCedula(cedulaCarnetComparacion, cedulaComparacion, cedulavalue);

        if(validacionCedulasvector[0]==1){
            //se hace visible la imagen del check
           // showToast("validacion correcta, con un porcentaje de: "+validacionCedulasvector[1]+"%");
            imgProgresIndicator.setImageResource(R.drawable.cheque);
            imgProgresIndicator.setColorFilter(ContextCompat.getColor(getContext(),
                    R.color.color_green));
        }else{
            //showToast("validacion Incorecta, con un porcentaje de: "+validacionCedulasvector[1]+"%");
            imgProgresIndicator.setImageResource(android.R.drawable.ic_delete);
            imgProgresIndicator.clearColorFilter();
            imgProgresIndicator.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_red));
        }

        if (validacion) {
            showToast("Error al Leer el documento del Carnet de Vacunación del Covid-19\n Por Favor Tomar la foto Nuevamente.");
        }

    }

    private void cargarBarraProgresoComparacion() {
        final Handler handler = new Handler();
        progressIndicator.setVisibility(View.VISIBLE);
        imgProgresIndicator.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressIndicator.setVisibility(View.GONE);
                imgProgresIndicator.setVisibility(View.VISIBLE);
            }
        }, 5000);
    }

    private void cargarBarraProgreso() {
        final Handler handler = new Handler();
        progressIndicator.setVisibility(View.VISIBLE);
        imgProgresIndicator.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressIndicator.setVisibility(View.GONE);
            }
        }, 2000);
    }

    public String[] ConvertirNumCarnet(String[] num) {

        int[] numero = new int[num.length];

        for (int i = 0; i < num.length; i++) {
            numero[i] = -1;
            try {
                numero[i] = Integer.parseInt(num[i]);
            } catch (NumberFormatException e) {
                num[i] = num[i].replaceAll("[^0-1-2-3-4-5-6-7-8-9]", "0");
                numero[i] = Integer.parseInt(num[i]);
            }
        }
        String[] datosCedula = new String[num.length];
        for (int i = 0; i < num.length; i++) {
            datosCedula[i] = String.valueOf(numero[i]);
        }
        return datosCedula;
    }

    public double[] validar(String[] datosCarnetVector, String[] datoComparar, String textoComparar) {
        double porcentajeValido = 0.0;
        double[] porcentajesDatos = new double[datosCarnetVector.length];

        for (int k = 0; k < datosCarnetVector.length; k++) {
            String[] parts = datosCarnetVector[k].split("");
            porcentajeValido = 0;

            if (datosCarnetVector[k].equals(textoComparar)) {
                porcentajeValido = 100.0;
            } else {
                if (datosCarnetVector[k].length() >= datoComparar.length - 1 && datosCarnetVector[k].length() <= datoComparar.length + 1) {
                    for (int j = 0; j < parts.length; j++) {
                        try {
                            if (parts[j].equals(datoComparar[j])) {
                                porcentajeValido = porcentajeValido + ((100 * 1.00) / datoComparar.length);
                            }
                        } catch (Exception e) {
                        }
                    }
                } else {
                    porcentajeValido = 0.0;//bien :)  si
                }
            }
            if (porcentajeValido == 100) {
                porcentajesDatos[k] = porcentajeValido;
                break;
            } else {
                porcentajesDatos[k] = porcentajeValido;
            }

        }
        int posNumMayor = porcentajeMayor(porcentajesDatos);
        double[] resultado = new double[3];

        if (porcentajesDatos[posNumMayor] >= 80) {
            resultado[0] = 1;
            resultado[1] = posNumMayor;
            resultado[2] = porcentajesDatos[posNumMayor];
            return resultado;
        } else {
            resultado[0] = 0;
            resultado[1] = posNumMayor;
            resultado[2] = porcentajesDatos[posNumMayor];
            return resultado;
        }
    }

    public double[] validarCedulaCarnet(String[] datosCarnetVector, String[] datoComparar, String textoComparar) {

        double porcentajeValido = 0.0;
        double[] porcentajesDatos = new double[datosCarnetVector.length];


        for (int k = 0; k < datosCarnetVector.length; k++) {
            datosCarnetVector[k] = datosCarnetVector[k].replace(".", "");
            String[] parts = datosCarnetVector[k].split("");
            porcentajeValido = 0;
            String[] datosCarnet = ConvertirNumCarnet(parts);
            if (datosCarnetVector[k].equals(textoComparar)) {
                porcentajeValido = 100.0;
            } else {
                if (datosCarnetVector[k].length() >= datoComparar.length - 1 && datosCarnetVector[k].length() <= datoComparar.length + 1) {
                    for (int j = 0; j < datosCarnet.length; j++) {
                        try {
                            if (datosCarnet[j].equals(datoComparar[j])) {
                                porcentajeValido = porcentajeValido + ((100 * 1.0) / datoComparar.length);
                            }

                        } catch (Exception e) {
                        }
                    }
                } else {
                    porcentajeValido = 0.0;//bien :)  si
                }
            }
            if (porcentajeValido == 100) {
                porcentajesDatos[k] = porcentajeValido;
                break;
            } else {
                porcentajesDatos[k] = porcentajeValido;
            }

            //  showToast(datosCarnetVector[k] + " Porcentaje: " + porcentajeValido);
        }
        int posNumMayor = porcentajeMayor(porcentajesDatos);
        double[] resultado = new double[4];


        if (porcentajesDatos[posNumMayor] >= 50) {
            resultado[0] = 1;
            resultado[1] = posNumMayor;
            resultado[2] = porcentajesDatos[posNumMayor];
            return resultado;
        } else {
            resultado[0] = 0;
            resultado[1] = posNumMayor;
            resultado[2] = porcentajesDatos[posNumMayor];
            return resultado;
        }
    }

    public double[] compararCedula(String datosCarnetVector, String datoscedula, String datoComparar) {
        double porcentajeValido = 0.0;
        datosCarnetVector = datosCarnetVector.replace(".", "");
        String[] cedulaCarnetdividida = datosCarnetVector.split("");
        String[] ceduladividida = datoscedula.split("");

//        for(int i=0; i< cedulaCarnetdividida.length ; i++ ){
//            showToast("prueba "+ cedulaCarnetdividida[i]);
//        }
        //String[] datosCarnet = ConvertirNumCarnet(cedulaCarnetdividida);
        if (datoscedula.equals(datoComparar)) {
            porcentajeValido = 100.0;
        } else {
            if (ceduladividida.length >= datoComparar.length() - 1 && ceduladividida.length <= datoComparar.length() + 1) {
                for (int j = 0; j < ceduladividida.length; j++) {
                    try {
                        if (ceduladividida[j].equals(ceduladividida[j])) {
                            porcentajeValido = porcentajeValido + ((100 * 1.0) / datoComparar.length());
                        }

                    } catch (Exception e) {
                        // showToast("El Error se encuentra en la posicion" + j + " " + cedulaCarnetdividida[j]);
                    }
                }
            } else {
                porcentajeValido = 0.0;
            }
        /*    if (datosCarnet.length >= datoComparar.length() - 1 && ceduladividida.length <= datoComparar.length() + 1) {
                for (int j = 0; j < ceduladividida.length; j++) {
                    try {
                        if (ceduladividida[j].equals(datosCarnet[j])) {
                            porcentajeValido = porcentajeValido + ((100 * 1.0) / datoComparar.length());
                        }

                    } catch (Exception e) {
                        // showToast("El Error se encuentra en la posicion" + j + " " + cedulaCarnetdividida[j]);
                    }
                }
            } else {
                porcentajeValido = 0.0;
            }*/
        }

        double[] resultado = new double[2];

        showToast("Porcentaje octenido :" + porcentajeValido);
        if (porcentajeValido >= 80) {
            resultado[0] = 1;
            resultado[1] = porcentajeValido;
            return resultado;
        } else {
            resultado[0] = 0;
            resultado[1] = porcentajeValido;
            return resultado;
        }
    }

    //En cuntra en el vector cual de todas la posiciones tiene un porcentaje mallo para luego retornar su posicion y saver qeu tento el que tiene el mallor porcentaje  es decir lo que buscamos MIVacuna
    private int porcentajeMayor(double[] vectorCorecto) {
        double numMayor = 0;
        int posNumMayor = 0;
        for (int i = 0; i < vectorCorecto.length; i++) {
            if (numMayor <= vectorCorecto[i]) {
                numMayor = vectorCorecto[i];
                posNumMayor = i;
            }
        }
        return posNumMayor;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private Integer getmImageMaxWidth() {
        if (mImageMaxWidth == null) {
            mImageMaxWidth = mImageView.getWidth();
        }
        return mImageMaxWidth;
    }

    private Integer getmImageMaxHeight() {
        if (mImageMaxHeight == null) {
            mImageMaxHeight = mImageView.getWidth();
        }
        return mImageMaxHeight;
    }

    private Pair<Integer, Integer> getTargetedWidthHeight() {
        int targetWidth;
        int targetHeight;
        int maxWidthForPortraidMode = getmImageMaxWidth();
        int maxHeightForPortraidMode = getmImageMaxHeight();
        targetWidth = maxWidthForPortraidMode;
        targetHeight = maxHeightForPortraidMode;
        return new Pair<>(targetWidth, targetHeight);
    }

    public void onItemSelected(Bitmap imgBitmap) {
        mSelectedImage = imgBitmap;
        if (mSelectedImage != null) {
            Pair<Integer, Integer> targetedSize = getTargetedWidthHeight();
            int targeteWidth = targetedSize.first;
            int maxHeight = targetedSize.second;
            float scaleFactor = Math.max((float) mSelectedImage.getWidth() / (float) targeteWidth,
                    (float) mSelectedImage.getHeight() / (float) maxHeight);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(mSelectedImage,
                    (int) (mSelectedImage.getWidth() / scaleFactor),
                    (int) (mSelectedImage.getHeight() / scaleFactor), true);
            //mImageView.setImageBitmap(resizedBitmap);
            mSelectedImage = resizedBitmap;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void verificarPermisosCamara() {
        int permisosCamara = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        int permisosAlmacenamientoEditar = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisosAlmacenamientoLeer = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permisosCamara == PackageManager.PERMISSION_GRANTED && permisosAlmacenamientoEditar == PackageManager.PERMISSION_GRANTED && permisosAlmacenamientoLeer == PackageManager.PERMISSION_GRANTED) {
            // showToast("Permiso de la camara otorgado");
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }


}