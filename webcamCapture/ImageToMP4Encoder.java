package webcamCapture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.jcodec.codecs.h264.H264Encoder;
import org.jcodec.codecs.h264.H264Utils;
import org.jcodec.codecs.wav.WavOutput.File;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.Brand;
import org.jcodec.containers.mp4.MP4Packet;
import org.jcodec.containers.mp4.TrackType;
import org.jcodec.containers.mp4.muxer.MP4Muxer;
import org.jcodec.containers.mp4.muxer.FramesMP4MuxerTrack;
import org.jcodec.scale.RgbToYuv420p;
import org.jcodec.scale.ColorUtil;


public class ImageToMP4Encoder {
	public void imageToMP4(BufferedImage bi, File file) {
		   // A transform to convert RGB to YUV colorspace
	    RgbToYuv420p transform = new RgbToYuv420p(0, 0);

	    // A JCodec native picture that would hold source image in YUV colorspace
	    Picture toEncode = Picture.create(bi.getWidth(), bi.getHeight(), ColorSpace.YUV420);

	    // Perform conversion
	    transform.transform(ColorUtil.fromBufferedImage(bi), yuv);

	    // Create MP4 muxer
	    MP4Muxer muxer = new MP4Muxer(sink, Brand.MP4);

	    // Add a video track
	    FramesMP4MuxerTrack outTrack = muxer.addTrack(TrackType.VIDEO, 25);//EHHH

	    // Create H.264 encoder
	    H264Encoder encoder = new H264Encoder(rc);

	    // Allocate a buffer that would hold an encoded frame
	    ByteBuffer _out = ByteBuffer.allocate(ine.getWidth() * ine.getHeight() * 6);

	    // Allocate storage for SPS/PPS, they need to be stored separately in a special place of MP4 file
	    List<ByteBuffer> spsList = new ArrayList<ByteBuffer>();
	    List<ByteBuffer> ppsList = new ArrayList<ByteBuffer>();

	    // Encode image into H.264 frame, the result is stored in '_out' buffer
	    ByteBuffer result = encoder.encodeFrame( toEncode, _out);

	    // Based on the frame above form correct MP4 packet
	    H264Utils.encodeMOVPacket(result, spsList, ppsList);

	    // Add packet to video track
	    outTrack.addFrame(new MP4Packet(result, 0, 25, 1, 0, true, null, 0, 0));

	    // Push saved SPS/PPS to a special storage in MP4
	    outTrack.addSampleEntry(H264Utils.createMOVSampleEntry(spsList, ppsList));

	    // Write MP4 header and finalize recording
	    muxer.writeHeader();
	}
}
