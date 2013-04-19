import vrml.*;
import vrml.node.*;
import vrml.field.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class X3D_CSV_Logger_TypeKinect extends Script {

	private MFVec3f joints;
	private File file = new File(System.getProperty("user.home")
			+ "\\PhysicsKinectLog.csv");

	List<SFVec3f> jointList = new ArrayList<SFVec3f>();
	List<SFTime> timeStampList = new ArrayList<SFTime>();
	double distanceTotal;
	double distance;

	double x;
	double y;
	double z;
	
	double delta;
	double speed;
	
	double oldDistance;
	double oldSpeed;
	double acc_var1;
	double acc_var2;

	public void initialize() {
		joints = (MFVec3f) getEventIn("logJoints");
		x = 0;
		y = 0;
		z = 0;
		distanceTotal = 0;
		distance = 0;
		delta = 0;
		speed = 0;
		oldDistance = -1;
		oldSpeed = 0;
		acc_var1 = 0;
		acc_var2 = 0;
	}

	public void processEvent(Event E) {
		if (E.getName().equals("logJoints")) {
			
			//Systemzeit:         1366201985712 s
			//SFTime Zeitstempel: 1.3662019857129998E9 stfimt	
			//1366201985 712 in ms
			//1366201985.7129998 in s			
			
			//System.out.println(E.getTimeStamp() );
			//System.out.println(((double)System.currentTimeMillis())/1000000 + " System");				
//			System.out.println(System.currentTimeMillis() + " s");
//			SFTime test = new SFTime(E.getTimeStamp());
//			System.out.println(test.getValue() + " stfimt");
			SFVec3f rightHand = new SFVec3f();
			joints.get1Value(11, rightHand);
			System.out.println(rightHand.getX());
			try {
				jointList.add((SFVec3f) rightHand.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			timeStampList.add(new SFTime(E.getTimeStamp()));
		}
	}
	

	

	public void shutdown() {
		// Write final data in .csv
		// ~800kb/min = ~1100 datasets/min?
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			// Prepare Table
			writer.append("rHand_X;");
			writer.append("rHand_Y;");
			writer.append("rHand_Z;");
			writer.append("distanceTotal;");			
			writer.append("distance to t-1 in m;");
			writer.append("speed in m/s;");
			writer.append("acc_var1 in m/s^2;");
			writer.append("acc_var2 in m/s^2;");
			writer.append("timeInSec;");
			writer.append("\n");

			System.out.println(jointList.size() == timeStampList.size());

			for (int i = 0; i < jointList.size(); i++) {

				writer.append(String.valueOf(jointList.get(i).getX() + ";").replace(".", ","));
				writer.append(String.valueOf(jointList.get(i).getY() + ";").replace(".", ","));
				writer.append(String.valueOf(jointList.get(i).getZ() + ";").replace(".", ","));

				if (i != 0) {
					x = jointList.get(i).getX() - jointList.get(i - 1).getX();
					y = jointList.get(i).getY() - jointList.get(i - 1).getY();
					z = jointList.get(i).getZ() - jointList.get(i - 1).getZ();

					//distance in m
					distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
					distanceTotal += distance;
				}
				writer.append(String.valueOf(distanceTotal + ";").replace(".", ","));
				writer.append(String.valueOf(distance + ";").replace(".", ","));
				
				if (i != 0) {
					//delta in m, timeStamp von s auf ms  -> *1000
					delta =  timeStampList.get(i).getValue() - timeStampList.get(i-1).getValue();
					speed = (distance) / (delta); // Geschwindigkeit in m/s
				}
				writer.append(String.valueOf(speed + ";").replace(".", ","));
				
				if (oldDistance != -1) {	
					acc_var1 = (2*((distance+oldDistance) - oldSpeed*(delta) - oldDistance)) 
							/ (Math.pow(delta, 2));
//					
//					
//					System.out.println("dividend" + 2*((distance+oldDistance) - oldSpeed*(delta) - oldDistance));
//					System.out.println("divisor " + Math.pow(delta, 2));
//					System.out.println("distance " + distance);
//					System.out.println("delta " + delta);
//					System.out.println("oldspeed " + oldSpeed);
//					System.out.println("olddistance " +  oldDistance);
//					System.out.println("ergebnis " + acc_var1);
//					System.out.println("############");
					
					if (i > 2) {
					acc_var2 = (speed - oldSpeed) / (((timeStampList.get(i).getValue()) - (timeStampList.get(i-2).getValue())) / 2);
					}
				}
			
				writer.append(String.valueOf(acc_var1 + ";").replace(".", ","));
				writer.append(String.valueOf(acc_var2 + ";").replace(".", ","));
				writer.append(String.valueOf(timeStampList.get(i).getValue() - timeStampList.get(0).getValue() + "\n").replace(".", ","));

				oldDistance = distance;
				oldSpeed = speed;	
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}