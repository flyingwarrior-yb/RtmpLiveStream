package com.androidyuan.publisher.param;

public class AudioParam {

	private int sampleRate = 44100;
	private int channel = 1;

	public AudioParam(int sampleRate, int channel) {
		this.sampleRate = sampleRate;
		this.channel = channel;
	}

	public AudioParam() {
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public int getChannel() {
		return channel;
	}
}
