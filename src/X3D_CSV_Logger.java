import vrml.*;
import vrml.node.*;
import vrml.field.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class X3D_CSV_Logger extends Script {

	private MFVec3f joints;
	private File file = new File(System.getProperty("user.home")
			+ "\\kinectLog.csv");

	List<MFVec3f> jointList = new ArrayList<MFVec3f>();

	public void initialize() {
		joints = (MFVec3f) getEventIn("logJoints");

	}

	public void processEvent(Event E) {
		if (E.getName().equals("logJoints")) {

			jointList.add(joints);
		}
	}

	public void shutdown() {
		// Write final data in .csv
		//~800kb/min = ~1100 datasets/min
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			// Prepare Table
			// "XN_SKEL_HEAD":0,
			// "XN_SKEL_NECK":1,
			// "XN_SKEL_TORSO":2,
			// "XN_SKEL_WAIST":3,
			// "XN_SKEL_LEFT_COLLAR":4,
			// "XN_SKEL_LEFT_SHOULDER":5,
			// "XN_SKEL_LEFT_ELBOW":6,
			// "XN_SKEL_LEFT_WRIST":7,
			// "XN_SKEL_LEFT_HAND":8,
			// "XN_SKEL_LEFT_FINGERTIP":9,
			// "XN_SKEL_RIGHT_COLLAR":10,
			// "XN_SKEL_RIGHT_SHOULDER":11,
			// "XN_SKEL_RIGHT_ELBOW":12,
			// "XN_SKEL_RIGHT_WRIST":13,
			// "XN_SKEL_RIGHT_HAND":14,
			// "XN_SKEL_RIGHT_FINGERTIP":15,
			// "XN_SKEL_LEFT_HIP":16,
			// "XN_SKEL_LEFT_KNEE":17,
			// "XN_SKEL_LEFT_ANKLE":18,
			// "XN_SKEL_LEFT_FOOT":19,
			// "XN_SKEL_RIGHT_HIP":20,
			// "XN_SKEL_RIGHT_KNEE":21,
			// "XN_SKEL_RIGHT_ANKLE":22,
			// "XN_SKEL_RIGHT_FOOT":23

			int i = 0;
			for (i = 0; i < 23; i++) {
				writer.append("joint[" + i + "][x];");
				writer.append("joint[" + i + "][y];");
				writer.append("joint[" + i + "][z];");

			}
			// Finish Table
			i++;
			writer.append("joint[" + i + "][x];");
			writer.append("joint[" + i + "][y];");
			writer.append("joint[" + i + "][z]");
			writer.append("\n");

			MFVec3f singleJoint = new MFVec3f();
			SFVec3f jointPart = new SFVec3f();

			for (i = 0; i < jointList.size(); i++) {
				int j = 0;
				singleJoint = jointList.get(i);
				for (j = 0; j < 23; j++) {
					singleJoint.get1Value(j, jointPart);

					writer.append(String.valueOf(jointPart.getX() + ";"));
					writer.append(String.valueOf(jointPart.getY() + ";"));
					writer.append(String.valueOf(jointPart.getZ() + ";"));
				}
				i++;
				writer.append(String.valueOf(jointPart.getX() + ";"));
				writer.append(String.valueOf(jointPart.getY() + ";"));
				writer.append(String.valueOf(jointPart.getZ() + "\n"));
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}