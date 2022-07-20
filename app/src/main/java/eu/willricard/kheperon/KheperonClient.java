package eu.willricard.kheperon;

import android.util.Log;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

public class KheperonClient {
  private final KheperonGrpc.KheperonBlockingStub mStub;

  public KheperonClient(Channel channel) {
    mStub = KheperonGrpc.newBlockingStub(channel);
  }

  public SectionConfigs enumSections() {
    EnumRequest request = EnumRequest.newBuilder().setSecret("toto").build();
    SectionConfigs response = null;

    try {
      response = mStub.enumSections(request);
    } catch (StatusRuntimeException e) {
      Log.w("kheperon:rpc", String.format("RPC failed: %s", e.getStatus()));
    }
    return response;
  }
}
