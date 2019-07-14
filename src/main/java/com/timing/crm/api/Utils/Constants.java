package com.timing.crm.api.Utils;

import org.springframework.http.MediaType;

public interface Constants {

    public static final String INACTIVO = "INACTIVO";

    public static final String ACTIVO = "ACTIVO";

    public static final Integer EQUAL = 0;

    public static final Integer DIFFERENT = 1;

    public static Integer EMPTY_ID = -1;

    public static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public static final String ERROR_FILE_SIZE = "File is too large";

    public static final String XLSX = "xlsx";

    public static final String XLS = "xls";

    //STATUS CALL
    public static final Integer CITA = 1;
    public static final Integer NOCONTESTVB = 2;
    public static final Integer LLAMAR_LUEGO = 3;
    public static final Integer EQUIVOCADO = 4;
    public static final Integer NO_CALIFICA_PARA_DEMO = 5;
    public static final Integer TIENE_REPRESENTANTE_RP = 6;
    public static final Integer ESTA_MOLESTO_CON_RP = 7;
    public static final Integer NUMERO_NO_EXISTE = 8;
    public static final Integer NO_INTERESADO = 9;

    //STATUS LEADS
    public static final Integer STATUS_INIT = 0;
    public static final Integer CLIENTEPROCESADO = 1;
    public static final Integer ENESPERADERESPUESTA = 2;
    public static final Integer ENRELLAMADA = 3;
    public static final Integer ENVALIDACION = 4;
    public static final Integer DESCALIFICADO = 5;

    //STATUS APPOINTMENT
    public static final Integer PORGARANTIZAR = 1;
    public static final Integer PORCORREGIR = 2;
    public static final Integer PORCONFIRMAR = 3;
    public static final Integer CONFIRMADACLIENTE = 4;
    public static final Integer RESULTADOREGISTRADO = 4;


    //Roles
    public static final Integer MANAGER = 1;
    public static final Integer COORDINATOR = 2;
    public static final Integer TELEMARKETER = 3;
    public static final Integer REPRESENTATIVE = 4;

    // Approach Type
    public static final String ABORDAJE = "ABORDAJE";
    public static final String TOQUE_PUERTA = "TOQUE DE PUERTA";
    public static final String CUATRO_14 = "4 en 14";
    public static final String REFERIDO = "REFERIDO";
    public static final String CLIENTE = "CLIENTE";

    // Approach Category
    public static final String PROSPECTO = "PROSPECTO";
    public static final String ANFITRION_4_14 = "ANFITRION 4-14";
    public static final String REFERIDO_4_14 = "REFERIDO 4-14";
    public static final String REFERIDO_PROMO = "REFERIDO PARA PROMOCION";

    //TypeSelectLeads
    public static final Integer NORMAL_SELECT = 0;
    public static final Integer LEADS_APP_TO_CALL = 1;
    public static final Integer LEADS_APP_TO_COORDINATE = 2;

    public static final String INVALID_FILE_CONTENT = "Invalid file content";

    // File Upload status
    public  static final int STATUS_RECEIVED = 0;
    public  static final int STATUS_PARTIAL = 1;
    public  static final int STATUS_OK = 2;
    public  static final int STATUS_FAIL = 3;

    // Report Representative Summary constants
    public static final String REP_NAME = "CLIENTE:";
    public static final String START_DATE = "Fecha Inicio";
    public static final String END_DATE = "Fecha Fin";
    public static final String TOTAL_LEADS = "Total Leads";
    public static final String TOTAL_APPOINTMENTS = "Total Citas";
    public static final String NO_ANSWER = "No Responde";
    public static final String CALL_LATER = "Llamar Luego";
    public static final String WRONG_NUMBER = "Numero Equivocado";
    public static final String NO_DEMO = "No Demo";
    public static final String HAS_REP_RP = "Tiene Representante RP";
    public static final String IS_UPSET = "Esta Molesto";
    public static final String PENDING_TO_CALL = "Leads Pendientes por Llamar";

    // Report Representative Details constants
    public static final String FECHA_DATA = "Fecha Data";
    public static final String ESTRELLAS = "Rating";
    public static final String NOMBRE = "Nombre";
    public static final String ESTADO_CIVIL = "Edo. Civil";
    public static final String TELEFONO_1= "Telefono 1";
    public static final String TELEFONO_2 = "Telefono 2";
    public static final String ZIP_CODE = "Zip Code";
    public static final String CIUDAD = "Ciudad";
    public static final String VENDEDOR = "Vendedor";
    public static final String TIPO_PROSPECCION = "Tipo de Prospeccion";
    public static final String LUGAR_ABORDAJE = "Lugar del Abordaje";
    public static final String NOTA_ABORDAJE = "Nota del Abordaje";
    public static final String OTROS_COMENTARIOS = "Otros Comentarios";
    public static final String NOTA_ABORDAJE_4_14 = "NOTA DEL ABORDAJE 4-14";
    public static final String DIRECCION = "Direccion";
    public static final String STATUS = "Estatus";
    public static final String FECHA_CITA = "Fecha Cita";
    public static final String FECHA_HORA_LLAMADA = "Fecha y hora de llamada";
    public static final String NOTAS_OPERADOR = "Notas Importantes Del Teleoperador";
    public static final String NOTAS_COORDINADOR = "Notas Importantes Del Coordinador";


}

