package com.sparkfighters.shared.lsd.fragments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface LSDFragment {
	void toStream(ByteArrayOutputStream bs) throws IOException;
	void fromStream(ByteArrayInputStream bs) throws IOException;
}
