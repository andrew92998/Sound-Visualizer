import java.io.*;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

public class Runner {
	static File audio;
	static AudioInputStream stream;
	static double[] audioFrames;
	static double[] audioFrames2;
	static double[] freqDomain;
	static double[] freqDomain2;
	static ArrayList<String> notes;
	static int[][] array;

	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Graph graph = new Graph();
		testFrame.add(graph);
		testFrame.setSize(1000, 800);
		testFrame.setVisible(true);
		try {
			array = new int[100000][1000];
			notes = new ArrayList<String>();
			audio = new File("Frame_Of_Mind.wav");
			stream = AudioSystem.getAudioInputStream(audio);
			AudioInputStream copy = AudioSystem.getAudioInputStream(audio);
			Clip clip = AudioSystem.getClip();
			clip.open(copy);
			clip.start();
			byte[] temp = new byte[stream.available()];
			stream.read(temp, 0, stream.available());
			int numChannels = stream.getFormat().getChannels();
			long numFrames = stream.getFrameLength();
			audioFrames = new double[(int) numFrames / 3];
			audioFrames2 = new double[(int) numFrames / 3];
			for (int i = 0; i < numFrames / 3; i++) {
				audioFrames[i] = (int) temp[i * 3 * stream.getFormat().getFrameSize() + 1] * 256
						+ (int) temp[i * 3 * stream.getFormat().getFrameSize()];
				audioFrames2[i] = (int) temp[i * 3 * stream.getFormat().getFrameSize() + 3] * 256
						+ (int) temp[i * 3 * stream.getFormat().getFrameSize()+2];
			}

			int interval = 500;
			int calc = 640;
			long start = System.currentTimeMillis();
			int frameCount = 1;
			for (int i = 0; i < audioFrames.length - interval; i += interval) {
				double[] temp2 = new double[calc];
				double[] temp22 = new double[calc];
				for (int j = 0; j < calc; j++) {
					temp2[j] = audioFrames[i + j];
					temp22[j] = audioFrames2[i + j];
				}
				freqDomain = fourier(temp2);
				//freqDomain2 = fourier(temp22);
				freqDomain2 = new double[freqDomain.length];
				int size = 1000;
				int[] temp3 = new int[size];
				int[] temp32 = new int[size];
				int[] temp4 = new int[size];
				int[] temp42 = new int[size];

				abs(freqDomain);
				// filter(freqDomain);
				norm(freqDomain, 100);
				exag(freqDomain, 1.2);
				cutoff(freqDomain, -20);
				
				abs(freqDomain2);
				// filter(freqDomain);
				norm(freqDomain2, 100);
				exag(freqDomain2, 1.2);
				cutoff(freqDomain2, -20);
				
				
				for (int j = 0; j < size / 10 - 10; j++) {
					int val = (int) Math.abs((freqDomain[j]));
					int val2 = (int) Math.abs((freqDomain2[j]));
					temp3[j * 10] = val;
					temp3[j * 10 + 1] = val;
					temp3[j * 10 + 2] = val;
					temp3[j * 10 + 3] = val;
					temp3[j * 10 + 4] = val;
					
					temp32[j * 10] = val2;
					temp32[j * 10 + 1] = val2;
					temp32[j * 10 + 2] = val2;
					temp32[j * 10 + 3] = val2;
					temp32[j * 10 + 4] = val2;
				}
				for (int b = 0; b < temp3.length; b++)
					array[frameCount][b] = temp3[b];

				if (frameCount > 10) {
					for (int b = 0; b < 3; b++) {
						for (int k = 0; k < 1000; k++) {
							if (array[frameCount - b][k] > temp4[k]) {
								temp4[k] = array[frameCount - b][k];
								temp42[k] = array[frameCount - b][k];
							}
						}
					}
				} else {
					for (int k = 0; k < 1000; k++) {
						temp4[k] = array[frameCount][k];
						temp42[k] = array[frameCount][k];
					}
				}

				graph.setValues(temp4);
				//graph.setValues2(temp42);
				
				graph.repaint();
				while (System.currentTimeMillis() - start < frameCount * 1000 / 44.1 * 1.5 -100) {
					System.out.println("waiting");
				}
				// double max = 0;
				// int ind = 0;
				// int ind2 = 0;
				// //int octave = 0;
				// for (int j = 0; j < freqDomain.length/10; j++) {
				// if (Math.abs(freqDomain[j]) > max) {
				// max = Math.abs(freqDomain[j]);
				// if (ind2 == ind) {
				// ind = j;
				// } else {
				// ind2 = ind;
				// ind = j;
				// max = Math.abs(freqDomain[j]);
				// }
				// }
				// }
				//
				// //System.out.println(((double)ind)*stream.getFormat().getSampleRate()/calc
				// + " " + freqDomain[ind] + " " +
				// ((double)ind2)*stream.getFormat().getSampleRate()/calc + " "
				// + freqDomain[ind2]);
				// double note1 =
				// ((double)ind)*stream.getFormat().getSampleRate()/calc*2;
				//
				// while (note1/2 > 16){
				// note1/=2;
				// //octave++;
				// }
				// System.out.println(note1);
				//
				// if((notes.size() > 0 &&
				// !notes.get(notes.size()-1).equals(calculateNote(note1))) ||
				// notes.size()== 0 ){
				// notes.add(calculateNote(note1));
				// System.out.println(calculateNote(note1));
				// }
				// }
				// for(String n:notes){
				// System.out.println(n);
				// }
				//
				frameCount++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void cutoff(double[] fd, int c) {
		for(int i=0; i<fd.length;i++){
			fd[i]-=c;
			if(fd[i]<0)
				fd[i]=0;
		}
		
	}

	private static void norm(double[] fd, double maxPerc) {

		for(int i=0; i<fd.length;i++){
				fd[i]=fd[i]/2800000 *400.0 * maxPerc/100.0;
		}
		
	}

	private static void exag(double[] fd, double power) {
		for (int i = 0; i < fd.length; i++) {
			fd[i] = Math.pow(fd[i], power)/Math.pow(400, power)*13000;
		}

	}

	// useless. a shame to delete it tho.
	private static void filter(double[] fd) {
		for (int i = 1; i < fd.length / 2; i++) {
			fd[i * 2] -= fd[i];
			fd[i * 2 + 1] -= fd[i];
			fd[i * 2 - 1] -= fd[i];
			if (fd[i * 2] < 0) {
				fd[i * 2] = 0;
			}
			if (fd[i * 2 + 1] < 0) {
				fd[i * 2 + 1] = 0;
			}
			if (fd[i * 2 - 1] < 0) {
				fd[i * 2 - 1] = 0;
			}
			if (i < fd.length / 3) {
				fd[i * 3] -= fd[i];
				fd[i * 3 + 1] -= fd[i];
				fd[i * 3 - 1] -= fd[i];
				if (fd[i * 3] < 0) {
					fd[i * 3] = 0;
				}
				if (fd[i * 3 + 1] < 0) {
					fd[i * 3 + 1] = 0;
				}
				if (fd[i * 3 - 1] < 0) {
					fd[i * 3 - 1] = 0;
				}
			}
			if (i < fd.length / 4) {
				fd[i * 4] -= fd[i];
				fd[i * 4 + 1] -= fd[i];
				fd[i * 4 - 1] -= fd[i];
				if (fd[i * 4] < 0) {
					fd[i * 4] = 0;
				}
				if (fd[i * 4 + 1] < 0) {
					fd[i * 4 + 1] = 0;
				}
				if (fd[i * 4 - 1] < 0) {
					fd[i * 4 - 1] = 0;
				}
			}
			if (i < fd.length / 5) {
				fd[i * 5] -= fd[i];
				fd[i * 5 + 1] -= fd[i];
				fd[i * 5 - 1] -= fd[i];
				if (fd[i * 5] < 0) {
					fd[i * 5] = 0;
				}
				if (fd[i * 5 - 1] < 0) {
					fd[i * 5 - 1] = 0;
				}
				if (fd[i * 5 + 1] < 0) {
					fd[i * 5 + 1] = 0;
				}
			}
		}

	}

	private static void abs(double[] fd) {
		for (int i = 0; i < fd.length; i++) {
			fd[i] = Math.abs(fd[i]);

		}

	}

	private static String calculateNote(double note) {
		while(note > 31.5){
			note/=2;
		}
		if (16 < note && note < 17)
			return "C";
		if (17 < note && note < 18)
			return "C#";
		if (18 < note && note < 19)
			return "D";
		if (19 < note && note < 20)
			return "D#";
		if (20 < note && note < 21.1)
			return "E";
		if (21.1 < note && note < 22.4)
			return "F";
		if (22.4 < note && note < 23.6)
			return "F#";
		if (23.6 < note && note < 25)
			return "G";
		if (25 < note && note < 26.5)
			return "G#";
		if (26.5 < note && note < 28)
			return "A";
		if (28 < note && note < 30)
			return "A#";
		if (30 < note && note < 31.5)
			return "B";
		else
			return "Too Low";

	}

	public static double[] fourier(double[] tD) {
		int n = tD.length;
		double[] fD = new double[n];
		for (int k = 0; k < n; k++) {
			double sumreal = 0;
			for (int t = 0; t < n; t++) {
				double angle = 2 * Math.PI * t * k * 1.2 / n;
				sumreal += tD[t] * Math.cos(angle);
			}
			fD[k] = sumreal;
		}
		return fD;
	}
}
