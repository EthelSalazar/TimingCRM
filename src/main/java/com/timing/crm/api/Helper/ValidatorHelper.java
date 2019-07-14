package com.timing.crm.api.Helper;

import com.timing.crm.api.View.QuestionRaw;
import com.timing.crm.api.View.QuestionValues;
import com.timing.crm.api.View.Questions;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static com.timing.crm.api.Utils.Constants.ABORDAJE;
import static com.timing.crm.api.Utils.Constants.ANFITRION_4_14;
import static com.timing.crm.api.Utils.Constants.CLIENTE;
import static com.timing.crm.api.Utils.Constants.CUATRO_14;
import static com.timing.crm.api.Utils.Constants.PROSPECTO;
import static com.timing.crm.api.Utils.Constants.REFERIDO;
import static com.timing.crm.api.Utils.Constants.REFERIDO_4_14;
import static com.timing.crm.api.Utils.Constants.REFERIDO_PROMO;
import static com.timing.crm.api.Utils.Constants.TOQUE_PUERTA;

public class ValidatorHelper {

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static boolean isValidRating(String rating) {
        boolean result = true;
        if (!rating.equalsIgnoreCase("*") && !rating.equalsIgnoreCase("**") && !rating.equalsIgnoreCase("***")) {
            result = false;
        }
        return result;
    }

    //TODO: hacerlo desde BD y dejarlo en cache
    public static Integer getApproachCode(String key) throws Exception {
        HashMap<String, Integer> approachMapper = new HashMap<>();
        Integer code;
        // approach type (tipo de prospeccion)
        approachMapper.put(ABORDAJE, 1);
        approachMapper.put(TOQUE_PUERTA, 2);
        approachMapper.put(CUATRO_14, 3);
        approachMapper.put(REFERIDO, 4);
        approachMapper.put(CLIENTE, 5);
        // prospect category (categoria de prospecto)
        approachMapper.put(PROSPECTO, 1);
        approachMapper.put(ANFITRION_4_14, 2);
        approachMapper.put(REFERIDO_4_14, 3);
        approachMapper.put(REFERIDO_PROMO, 4);
        code = approachMapper.get(keyNormalization(key));
        if (code==null) throw new Exception("Invalid Approach or Category value");
        return code;
    }


    public static String keyNormalization(String key) {
        String resultKey = key.trim();
        resultKey = resultKey.toUpperCase();
        if (resultKey.contains("S")) {
            if (resultKey.lastIndexOf("S") == resultKey.length()-1) {
                resultKey = resultKey.substring(0, resultKey.length()-1);
            }
        }
        return resultKey;
    }


    public static List<Questions> questionMapper(List<QuestionRaw> questionRawList) {
        List<Questions> questionsList = new ArrayList<>();
        List<QuestionValues> questionValuesList = new ArrayList<>();
        Integer questionForId=null;
        Questions questions=null;
        QuestionValues questionValues=null;
        for (QuestionRaw questionRaw:questionRawList){
            if (questionForId != questionRaw.getQuestionId()){
                if(questionForId!=null){
                    questions.setValuesList(questionValuesList);
                    questionsList.add(questions);
                }
                questionForId = questionRaw.getQuestionId();
                questions = new Questions();
                questionValuesList = new ArrayList<>();
                questions.setId(questionRaw.getQuestionId());
                questions.setDescription(questionRaw.getQuestionDescription());
                questions.setType(questionRaw.getQuestionType());
                questions.setDepending(questionRaw.getDepending());
                questions.setQuestionDependingId(questionRaw.getQuestionDependingId());
                questions.setExtraLabel(questionRaw.getExtraLabel());
                questions.setSpecialValidation(questionRaw.getSpecialValidation());

            }
            if (questionRaw.getValueId() != null){
                questionValues = new QuestionValues();
                questionValues.setId(questionRaw.getValueId());
                questionValues.setQuestionId(questionRaw.getQuestionId());
                questionValues.setDescription(questionRaw.getValueDescription());
                questionValues.setDesqualifying(questionRaw.getDesqualifying());
                questionValues.setResultCallId(questionRaw.getResultCallId());
                questionValuesList.add(questionValues);
            }
        }
        if(questionForId!=null){
            questions.setValuesList(questionValuesList);
            questionsList.add(questions);
        }
        return questionsList;
    }

    //TODO: normalizar la data en los data entries (upload, one by one)
    public static String cleanPhoneNumber(String phone) {
        String outPhone ="";
        outPhone = outPhone.replace("+", "");
        outPhone = phone.replace("-", "");
        outPhone = outPhone.replace("(", "");
        outPhone = outPhone.replace(")", "");
        outPhone = outPhone.replace(" ", "");
        return outPhone.trim();
    }

}
