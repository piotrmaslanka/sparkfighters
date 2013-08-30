package com.sparkfighters.shared.lsd.fragments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface LSDFragment {
	public void toStream(ByteArrayOutputStream bs) throws IOException;
	public void fromStream(ByteArrayInputStream bs) throws IOException;
	public int getId();
}
