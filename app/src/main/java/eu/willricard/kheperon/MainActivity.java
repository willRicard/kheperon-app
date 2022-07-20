package eu.willricard.kheperon;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

  private Spinner mSpinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mSpinner = findViewById(R.id.device_spinner);

    String target = "10.0.2.2:50051";
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

    try {
      KheperonClient client = new KheperonClient(channel);
      SectionConfigs response = client.enumSections();

      List<String> items =
          response.getConfigsList().stream()
              .map(SectionConfig::getDevicesList)
              .flatMap(List::stream)
              .map(DeviceConfig::getName)
              .collect(Collectors.toList());

      mSpinner.setAdapter(
          new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items));
    } finally {
      try {
        channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
