package com.sanqing.sca.service;

public class ReturnCode {
//	public static final int OTPR_CORE_COMM_SUIT 	= 0x80001000;
	//����ɹ�
	public static final String SUCCESS_CODE = "0";
	public static final String SUCCESS_REMSG = "�ɹ�";
	
	/***************************************
	 * ����˵ķ����쳣
	 */
	//���������ļ���ʽ����
	public static final String CONFIG_FILE_ERROR_CODE = "1001";
	public static final String CONFIG_FILE_ERROR_REMSG = "���������ļ���ʽ����";
	

	//����δ����
	public static final String TASK_NOT_CONFIG_CODE = "1002";
	public static final String TASK_NOT_CONFIG_REMSG = "����δ����";

	//ʵ����δ�ҵ�
	public static final String CLASS_IS_NULL_CODE = "1004";
	public static final String CLASS_IS_NULL_REMSG = "ҵ��ʵ����δ�ҵ�";
	
	//Э������ʶΪ��
	public static final String SERVICE_IS_NULL_CODE = "1005";
	public static final String SERVICE_IS_NULL_REMSG = "Э������ʶΪ��";

	//Э�������ʶΪ��
	public static final String TASK_IS_NULL_CODE = "1006";
	public static final String TASK_IS_NULL_REMSG = "Э�������ʶΪ��";
	
	//����ִ���쳣
	public static final String TASK_EXCEPTION_CODE = "1007";
	public static final String TASK_EXCEPTION_REMSG = "����ִ���쳣";
	
	//�޿��÷���
	public static final String NO_SERVICE_CODE = "1008";
	public static final String NO_SERVICE_REMSG = "�޿��÷���";

	
	/*********
	 * �ͻ��˴���������
	 */
	//�޿��÷�����
	public static final String NO_SERVER_MACHINE_CODE = "1101";
	public static final String NO_SERVER_MACHINE_REMSG = "�޿��÷�����";
	
	//ҵ����ʱ
	public static final String REQUEST_TIME_OUT_CODE = "1102";
	public static final String REQUEST_TIME_OUT_REMSG = "ҵ����ʱ";
	
	//ҵ�񷽷�ִ��δ��ʶ���
	public static final String WORK_EXECUTE_NULL_CODE = "1103";
	public static final String WORK_EXECUTE_NULL_REMSG = "δ��ʶ���";
	
	
	/**********
	 * ҵ����
	 */
	//�������ݲ��Ϸ�
	public static final String INPUT_DATA_ERROR_CODE = "1201";
	public static final String INPUT_DATA_ERROR_REMSG = "�������ݲ��Ϸ�";
	
	//���ݿ����ʧ��
	public static final String DATABASE_ERROR_CODE = "1202";
	public static final String DATABASE_ERROR_REMSG = "���ݿ����ʧ��";
	
	//��������
	public static final String OTHER_ERROR_CODE = "1203";
	public static final String OTHER_ERROR_REMSG = "ҵ����ʧ��";

}
