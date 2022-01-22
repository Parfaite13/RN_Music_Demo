import React from 'react'
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Picker from './picker'
import Music from './music'

const Stack = createNativeStackNavigator();

const App = () => {
  return  (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="Press button and pick an Audio..." component={Picker} />
        <Stack.Screen name="Music" component={Music} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
export default App