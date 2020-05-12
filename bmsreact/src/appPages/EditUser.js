import React, { useState} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button,
  TextField,
  MenuItem,
  Select,
  RadioGroup,
  FormControlLabel,
  Radio
} from "@material-ui/core";

import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';

import {restCall} from '../utils/RestComponent'

const editState = {

      };

const EditUser = props => {
    function handleChange(event) {
        if(event.target.id==='fname')
            editState.firstName = event.target.value;
        if(event.target.id==='lname')
            editState.lastName = event.target.value;
        if(event.target.id==='phone' && /^\d+$/.test(event.target.value))
            editState.phone = event.target.value;
        if(event.target.id==='postal')
             editState.postalCode = event.target.value;
        if(event.target.id==='addr1')
             editState.addr1 = event.target.value;
        if(event.target.id==='addr2')
            editState.addr2 = event.target.value;
        if(event.target.id==='answer')
            editState.secretAns = event.target.value;

        editState.cityId = props.userData.cityVo.id;
        editState.secretQues = props.userData.secretQues;
    };

    function handleTypeChange(event) {
        editState.type = event.target.value;
    }

    const [apiRet, setApiRet] = useState({});

    const editUser = () =>{
        editState.cityId = props.userData.cityVo.id;
        editState.secretQues = props.userData.secretQues;
        editState.id = props.userData.id;

        restCall('POST', `${props.serverURI}/api/updateUser`, setApiRet, props.token, editState);
    };

    return(
        <div
            style={{
            display: "flex",
            justifyContent: "center",
            margin: 20,
            padding: 20
            }}
        >
            <form style={{ width: "50%" }} onChange={handleChange.bind(this)}>
                <h1>Edit User</h1>
                {apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="emailId">Email Id</InputLabel>
                    <Input id="emailId" type="text" value={props.userData.emailId}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="fname">First Name</InputLabel>
                    <Input id="fname" type="text" defaultValue={props.userData.firstName}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="lname">Last Name</InputLabel>
                    <Input id="lname" type="text" defaultValue={props.userData.lastName}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="phone">Phone (Only numbers will be accepted.)</InputLabel>
                    <Input id="phone" type="text" defaultValue={props.userData.phone}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="add1">Address Line 1</InputLabel>
                    <Input id="addr1" type="text" defaultValue={props.userData.addr1}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="add2">Address Line 2</InputLabel>
                    <Input id="addr2" type="text" defaultValue={props.userData.addr2}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="postal">Postal Code</InputLabel>
                    <Input id="postal" type="text" defaultValue={props.userData.postalCode}/>
                </FormControl>

                {props.userData.type==='S'?(<>
                    <FormControl margin="normal" fullWidth>
                        <InputLabel htmlFor="type-lable">User type</InputLabel>
                        <Select
                          labelId="type"
                          id="type"
                          defaultValue={props.userData.type}
                          onChange={handleTypeChange}
                        >
                          <MenuItem value='U'>User</MenuItem>
                          <MenuItem value='A'>Admin</MenuItem>
                          <MenuItem value='S'>Super User</MenuItem>
                        </Select>
                    </FormControl>
                    <p/>
                    <FormControl>
                      <FormLabel component="legend">Is Sensei</FormLabel>
                      <RadioGroup aria-label="gender" name="gender1" defaultValue={props.userData.sesnei} onChange={handleChange}>
                        <FormControlLabel value="Y" control={<Radio />} label="Yes" />
                        <FormControlLabel value="N" control={<Radio />} label="No" />
                      </RadioGroup>
                    </FormControl></>
                ):(<></>)}

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="answer">What is your mothers maiden name</InputLabel>
                    <Input id="answer" type="password" />
                </FormControl>

                <Button variant="contained" color="primary" size="medium" onClick={editUser}>
                    Send
                </Button>
            </form>
        </div>
    );
}

export default EditUser;