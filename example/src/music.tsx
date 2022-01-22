import * as React from 'react'
import { useState} from 'react'
import { NativeModules, StyleSheet, View, NativeEventEmitter, Text, TouchableHighlight} from 'react-native'
import Volume from './volume';
import { useRoute } from '@react-navigation/native';

const { MDModule} = NativeModules;
const completeEvents = new NativeEventEmitter(MDModule);
const onComplete = 'onComplete'

const Music = () => {
  const route = useRoute();
  const [action, setAction] = useState<string>(MDModule.initial);

  React.useEffect(() => {
    completeEvents.addListener(onComplete, () => {
      setAction(action);
    });

    // cleanup this component
    return () => {
      completeEvents.removeAllListeners(onComplete);
    };
  }, []);

  const play = () => {
    console.log("play...")
    console.log(`${route.params.path}`)

    MDModule.playOrPause(route.params.path)
    .then( (action: string) => setAction(action))
    .catch( (error: any) => console.error(error));
  }

  const reset = () => {
    console.log("reset...")
    MDModule.reset()
  }

    return (
      <View style={styles.container}>
        <Text style={styles.titleText}>{route.params.name}</Text>
        <Separator />
        <View style={styles.fixToText}>
          <TouchableHighlight
            style={styles.buttonPlay}
            onPress={() => play()}
            underlayColor='#ac80ac'>
              <Text style={[styles.titleTextButton]}> {action} </Text>
          </TouchableHighlight>
          <SeparatorV />
          <TouchableHighlight
            style={styles.buttonReset}
            onPress={() => reset()}
            underlayColor='#804088'>
              <Text style={[styles.titleTextButton]}> Reset </Text>
          </TouchableHighlight>
        </View>
        <Separator />
        <Volume/>
      </View>
    );
};

const Separator = () => (
  <View style={styles.separator} />
);

const SeparatorV = () => (
  <View style={styles.separatorV} />
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  row: {
    flexDirection: "row",
    flexWrap: "wrap",
  },
  fixToText: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  buttonPlay:{
    backgroundColor: '#cfa0cf',
    alignItems: 'center',
    width: 100,
    overflow: 'hidden',
    fontSize: 30,
    color: 'white'
  },
  buttonReset:{
    backgroundColor: '#a162aa',
    alignItems: 'center',
    width: 100,
    overflow: 'hidden',
    fontSize: 30,
    color: 'white'
  },
  separator: {
    marginVertical: 16,
    borderBottomColor: '#737373',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  separatorV: {
    marginHorizontal: 8,
    borderBottomColor: '#737373',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  titleText: {
    fontSize: 30,
    fontWeight: "bold"
  },
  titleTextButton: {
    fontSize: 30,
    color: 'white'
  }
})
export default Music