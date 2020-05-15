import React, {useState} from 'react';

import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  TableContainer,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
  Button
} from "@material-ui/core";

import {restCall} from '../utils/RestComponent'


const ListUsers = props => {
    const [apiRet, setApiRet] = useState({});

    const handleChange=(event)=>{
        console.log(props.token);
        if(event.target.id==='lastName'){
            restCall('GET', `${props.serverURI}/api/findUser?lastName=${event.target.value}`, setApiRet, props.token, null);
        }
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
           <div style={{width:"70%"}}>
            <form onChange={handleChange.bind(this)}>
                <h1>List Users</h1>
                {apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="lastName">Last Name</InputLabel>
                    <Input id="lastName" type="text"/>
                </FormControl>
            </form>
            <p/>

            <TableContainer component={Paper}>
                <TableHead>
                  <TableRow>
                    <TableCell>First Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TableCell>
                    <TableCell>Last Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TableCell>
                    <TableCell>Email Id&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TableCell>
                    <TableCell>Phone&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TableCell>
                    <TableCell>&nbsp;</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                    {apiRet!= undefined
                        && apiRet!==null
                        && Object.getOwnPropertyNames(apiRet).length !== 0?(
                        <>
                            {apiRet.map(user=>(
                                <TableRow key='{user.id}'>
                                    <TableCell component="th" scope="row">
                                        {user.firstName}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {user.lastName}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {user.emailId}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {user.phone}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {user.id!=props.globalData.loginUser.id?(
                                            <Button>Edit</Button>
                                            ):(null)}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </>
                    ):(null)}
                </TableBody>
            </TableContainer>
            </div>
        </div>
    );
}

export default ListUsers;