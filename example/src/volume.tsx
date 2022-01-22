import * as React from 'react'
import { useState} from 'react'
import { NativeModules, Text, View, StyleSheet, TouchableHighlight } from 'react-native'

const { VolumeModule } = NativeModules;

const Volume = () => {
  const[vol, setVol] = useState(VolumeModule.initialVol)

  const decrement = () => {
    VolumeModule.decrement()
      .then( (vol : number) => setVol(vol))
      .catch( (error: any) => console.error(error));
  }

  const increment = () => {
    VolumeModule.increment()
    .then( (vol : number) => setVol(vol))
    .catch( (error: any) => console.error(error));
  }

  return (
      <View>
        <TouchableHighlight
          style={styles.submit}
          onPress={() => increment()}
          underlayColor='#4680ad'>
            <Text style={[styles.titleText]}> Vol Up </Text>
        </TouchableHighlight>
        <Text style={styles.titleText}>Volume {vol}</Text>
        <TouchableHighlight
          style={styles.submit}
          onPress={() => decrement()}
          underlayColor='#4680ad'>
            <Text style={[styles.titleText]}> Vol Down </Text>
        </TouchableHighlight>
      </View>
      )
}
const styles = StyleSheet.create({
  submit:{
    backgroundColor: '#68a0cf',
    overflow: 'hidden',
    alignItems: 'center',
    width:150,
    fontSize: 30,
    color: 'white'
  },
  titleText: {
    fontSize: 25,
    fontWeight: "bold"
  }
})
export default Volume