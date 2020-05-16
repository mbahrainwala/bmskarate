import React, { useState, useEffect} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button,
  MenuItem,
  Select,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper
} from "@material-ui/core";

import {restCall} from '../utils/RestComponent'

const EditUser = props => {
    const [apiRet, setApiRet] = useState({});
    const [editState, setEditState] = useState({

    });

    useEffect(() => {
        if(Object.getOwnPropertyNames(editState).length===0){
            restCall('GET', `${props.globalData.serverURI}/api/getUser?id=${props.globalData.editUser.id}`, setApiRet, props.globalData.token, null);
            if(apiRet!==null && apiRet.errors === undefined && apiRet.id!==undefined){
                setEditState(apiRet);
                console.log(editState);
            }
        }
    },[editState, apiRet])

    const editUser=()=>{
    }

    return(
        <div
            style={{
            display: "flex",
            justifyContent: "center",
            margin: 20,
            padding: 20
            }}
        >
        {Object.getOwnPropertyNames(editState).length !== 0 ?(
            <form style={{ width: "80%" }}>
                <h1>{editState.id===props.globalData.loginUser.id?(<>Your Profile</>):(<>Edit User</>)}</h1>
                <InputLabel htmlFor="emailId">Email Id</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="emailId" type="text" value={editState.emailId}/>
                </FormControl>


                <InputLabel htmlFor="fname">First Name</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="fname" type="text" value={editState.firstName}/>
                </FormControl>

                <InputLabel htmlFor="lname">Last Name</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="lname" type="text" value={editState.lastName}/>
                </FormControl>

                <InputLabel htmlFor="phone">Phone (XXX-XXX-XXXX)</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="phone" type="text" value={editState.phone}/>
                </FormControl>

                <InputLabel htmlFor="add1">Address Line 1</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="addr1" type="text" value={editState.addr1}/>
                </FormControl>

                <InputLabel htmlFor="add2">Address Line 2</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="addr2" type="text" value={editState.addr2}/>
                </FormControl>

                <InputLabel htmlFor="postal">Postal Code</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="postal" type="text" value={editState.postalCode}/>
                </FormControl>

                {editState.id===props.globalData.loginUser.id?(<>
                <FormControl margin="normal" fullWidth>
                    {props.globalData.editUser.type==='U'?(<>User</>):(null)}
                    {props.globalData.editUser.type==='A'?(<div style={{color:'blue'}}>Admin</div>):(null)}
                    {props.globalData.editUser.type==='S'?(<div style={{color:'red'}}>Super User</div>):(null)}
                </FormControl></>):(
                <>
                <InputLabel htmlFor="type-lable">User type</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Select
                      labelId="type"
                      id="type"
                      value={editState.type}
                    >
                      <MenuItem value='U'>User</MenuItem>
                      <MenuItem value='A'>Admin</MenuItem>
                      <MenuItem value='S'>Super User</MenuItem>
                    </Select>
                </FormControl>
                </>)}

                <InputLabel htmlFor="answer">What is your mothers maiden name</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="answer" type="password" />
                </FormControl>

                <InputLabel htmlFor="password">New Password</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="password" type="password" />
                </FormControl>

                <InputLabel htmlFor="confirmPassword">Confirm Password</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="confirmPassword" type="password" />
                </FormControl>

                {editState.id!==props.globalData.loginUser.id?(null):(
                    <FormControl margin="normal" fullWidth>
                        <InputLabel htmlFor="oldpass">Existing Password</InputLabel>
                        <Input id="oldpass" type="password" />
                    </FormControl>)}

                <Button variant="contained" color="primary" size="medium" onClick={editUser}>
                    Send
                </Button>
            </form>):null}
        </div>
    );
}

export default EditUser;