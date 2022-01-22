import * as React from 'react'
import { useState} from 'react'
import { Button, NativeModules, StyleSheet, View, FlatList,TouchableOpacity, Text, TouchableHighlight } from 'react-native'
import { useNavigation } from '@react-navigation/native';
import FadeInOut from 'react-native-fade-in-out';

const { PickerModule } = NativeModules;

export type AudioInfo = {
  name: string
  path: string
  duration: number | null
  size: number | null
}

export const Picker = () => {
  const navigation = useNavigation();
  const [response, setResponse] = useState<AudioInfo[]>([]);
  const [selectedPath, setSelectedPath] = useState('');
  const [visible, setVisible] = useState(true);

  const toggleVisible = () => {
    setVisible(!visible);
  }


  const view  = () => {
    toggleVisible()
    PickerModule.getFileList()
    .then( (data: AudioInfo[]) => setResponse(data))
    .catch( (error: any) => console.error(error))
    console.log(response.length)
  }

  const renderItem  = ({ item }) => {
    return (
      <TouchableOpacity onPress={() => navigateToMusic(item)} style={[styles.item]}>
        <Text style={styles.titleText}>{item.name}</Text>
      </TouchableOpacity>
    );
  }
  
  function navigateToMusic(item : AudioInfo) {
    setSelectedPath(item.path)
    navigation.navigate("Music",
                        { name : item.name, 
                          path : item.path });
  }

  return (
    <View style={styles.container}>
    <FadeInOut visible={visible}>
      <TouchableHighlight
        style={styles.submit}
        onPress={() => view()}
        underlayColor='#4680ad'>
          <Text style={[styles.titleText]}> Pick Audio </Text>
      </TouchableHighlight>
    </FadeInOut>
    <FadeInOut visible={!visible}>
      <FlatList 
        data={response}
        extraData={response}
        keyExtractor={(item) => item.path}
        renderItem={renderItem}
      />
    </FadeInOut>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'flex-start',
  },
  row: {
    flexDirection: "row",
    flexWrap: "wrap",
  },
  submit:{
    backgroundColor: '#68a0cf',
    overflow: 'hidden'
  },
  item: {
    backgroundColor: '#a162aa',
    padding: 20,
    marginVertical: 8,
    marginHorizontal: 16,
    borderRadius:5
  },
  titleText: {
    fontSize: 30,
    color: 'white',
  }
})

export default Picker