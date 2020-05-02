import * as React from 'react';
import { StyleSheet, View, Text } from 'react-native';
import BackgroundTrackGeolocation from 'react-native-background-track-geolocation';

export default function App() {
  React.useEffect(() => {
    BackgroundTrackGeolocation.startTracking();
  }, []);

  return (
    <View style={styles.container}>
      <Text>Device name: React native Background Track Geolocation</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
